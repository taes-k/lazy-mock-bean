package io.github.lazymockbean.parser;

import io.github.lazymockbean.annotation.LazyInjectMockBeans;
import io.github.lazymockbean.data.BeanDependencyFinder;
import io.github.lazymockbean.data.LazyMockDefinition;
import io.github.lazymockbean.data.LazyMockTarget;
import io.github.lazymockbean.utils.Annotations;
import io.github.lazymockbean.utils.MockGenerator;
import io.github.lazymockbean.utils.SpringBeans;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class LazyMockDefinitionParser {
    private static final List<String> EXCLUDE_BEAN_PACKAGE = Arrays.asList("java.util", "java.lang");

    public Set<LazyMockDefinition> parse(TestContext testContext) {
        return Arrays.stream(testContext.getTestClass().getDeclaredFields())
                .filter(Annotations::hasLazyMockBeanAnotations)
                .flatMap(mockingField -> {
                    Class<?> targetFieldType = mockingField.getType();
                    Object mock = MockGenerator.generate(mockingField, testContext, targetFieldType);
                    Class<?>[] injectTargets = Annotations.getInjectTargets(mockingField);
                    if (!ArrayUtils.isEmpty(injectTargets)) {
                        return parseSpecificTargets(injectTargets, testContext, mockingField, mock).stream();
                    } else {
                        return parseAutowiredDependencyBeans(testContext, mockingField, mock).stream();
                    }
                }).collect(Collectors.toSet());
    }

    private List<LazyMockDefinition> parseSpecificTargets(
            Class<?>[] injectTargets, TestContext testContext, Field mockingField, Object mockObject
    ) {
        return Arrays.stream(injectTargets)
                .flatMap(it -> generateDefinition(testContext, it, mockingField, mockObject).stream())
                .collect(Collectors.toList());
    }

    private List<LazyMockDefinition> generateDefinition(
            TestContext testContext, Class<?> target, Field mockingField, Object mockObject
    ) {
        return findMockingTarget(testContext, target, mockingField.getType()).stream()
                .map(it -> {
                    Object targetBean = it.getTargetBean();
                    Field targetField = it.getTargetField();
                    Object originValue = getFieldValue(targetBean, targetField);
                    return new LazyMockDefinition(mockingField, targetField, targetBean, originValue, mockObject);
                }).collect(Collectors.toList());
    }

    private Object getFieldValue(Object targetBean, Field targetField) {
        try {
            targetField.trySetAccessible();
            return targetField.get(targetBean);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private List<LazyMockTarget> findMockingTarget(TestContext testContext, Class<?> targetObject, Class<?> targetFieldType) {
        List<Object> beans = SpringBeans.findAllBeansWithoutProxy(testContext, targetObject);
        return beans.stream()
                .map(bean -> {
                    Field targetFieldInBean = ReflectionUtils.findField(bean.getClass(), null, targetFieldType);
                    if (targetFieldInBean == null) {
                        String message = String.format("not exist field [%s] in bean [%s]", targetFieldType.getName(), targetObject.getSimpleName());
                        throw new IllegalStateException(message);
                    }
                    return new LazyMockTarget(bean, targetFieldInBean);
                }).collect(Collectors.toList());
    }

    private List<LazyMockDefinition> parseAutowiredDependencyBeans(TestContext testContext, Field mockingField, Object mockObject) {
        Class<?> targetFieldType = mockingField.getType();
        BeanDependencyFinder beanDependencyFinder = new BeanDependencyFinder();

        Arrays.stream(testContext.getTestClass().getDeclaredFields())
                .filter(it -> it.isAnnotationPresent(LazyInjectMockBeans.class))
                .forEach(it -> beanDependencyFinder.push(it.getType()));

        if (beanDependencyFinder.isEmpty()) {
            Arrays.stream(testContext.getTestClass().getDeclaredFields())
                    .filter(it -> it.isAnnotationPresent(Autowired.class))
                    .forEach(it -> beanDependencyFinder.push(it.getType()));
        }

        List<LazyMockDefinition> mockDefinitions = new ArrayList<>();

        while (beanDependencyFinder.hasMore()) {
            Class<?> dependentClazz = beanDependencyFinder.pop();
            List<Object> beans = SpringBeans.findAllBeansWithoutProxy(testContext, dependentClazz);
            for (Object bean : beans) {
                List<LazyMockDefinition> definitions = Arrays.stream(bean.getClass().getDeclaredFields())
                        .filter(it -> it.getType() == targetFieldType)
                        .flatMap(it -> generateDefinition(testContext, dependentClazz, mockingField, mockObject).stream())
                        .collect(Collectors.toList());
                mockDefinitions.addAll(definitions);

                Arrays.stream(bean.getClass().getDeclaredFields())
                        .filter(it -> isValidBeanType(it.getType()))
                        .filter(it -> SpringBeans.hasAnyBean(testContext, it.getType()))
                        .forEach(it -> beanDependencyFinder.push(it.getType()));
            }
        }
        return mockDefinitions;
    }

    private Boolean isValidBeanType(Class<?> type) {
        if (type == null || type.getPackage() == null) {
            return false;
        }

        return !ClassUtils.isPrimitiveOrWrapper(type) &&
                !ClassUtils.isPrimitiveArray(type) &&
                !ClassUtils.isPrimitiveWrapperArray(type) &&
                !EXCLUDE_BEAN_PACKAGE.contains(type.getPackage().toString());
    }

}
