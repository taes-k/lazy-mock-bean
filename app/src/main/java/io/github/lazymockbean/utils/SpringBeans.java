package io.github.lazymockbean.utils;

import lombok.experimental.UtilityClass;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.context.TestContext;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class SpringBeans {
    @SuppressWarnings("unchecked")
    public Boolean hasAnyBean(TestContext testContext, Class<?> targetObject) {
        Map<String, Object> targetBeans = testContext.getApplicationContext().getBeansOfType((Class<Object>) targetObject);
        return targetBeans.values().size() > 0;
    }

    @SuppressWarnings("unchecked")
    public Object findBean(TestContext testContext, Class<?> targetObject) {
        Map<String, Object> targetBeans = testContext.getApplicationContext().getBeansOfType((Class<Object>) targetObject);
        if (targetBeans.isEmpty()) {
            String message = String.format("not exist bean [%s] in beanFactory", targetObject.getSimpleName());
            throw new IllegalStateException(message);
        }
        if (targetBeans.size() != 1) {
            String message = String.format("more than 1 bean [%s] in beanFactory", targetObject.getSimpleName());
            throw new IllegalStateException(message);
        }
        return targetBeans.values().iterator().next();
    }

    public List<Object> findAllBeansWithoutProxy(TestContext testContext, Class<?> targetObject) {
        return findAllBean(testContext, targetObject).stream().map(SpringBeans::unWrapProxy).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private List<Object> findAllBean(TestContext testContext, Class<?> targetObject) {
        Map<String, Object> targetBeans = testContext.getApplicationContext().getBeansOfType((Class<Object>) targetObject);
        return Arrays.asList(targetBeans.values().toArray());
    }

    private Object unWrapProxy(Object targetBeanCandidate) {
        if (AopUtils.isAopProxy(targetBeanCandidate)) {
            return AopProxyUtils.getSingletonTarget(targetBeanCandidate);
        } else {
            return targetBeanCandidate;
        }
    }
}
