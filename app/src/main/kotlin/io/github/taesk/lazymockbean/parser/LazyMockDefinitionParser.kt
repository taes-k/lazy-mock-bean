package io.github.taesk.lazymockbean.parser

import io.github.taesk.lazymockbean.annotation.LazyInjectMockBeans
import io.github.taesk.lazymockbean.data.DependencyFinder
import io.github.taesk.lazymockbean.data.LazyMockDefinition
import io.github.taesk.lazymockbean.data.LazyMockTarget
import io.github.taesk.lazymockbean.utils.Annotations
import io.github.taesk.lazymockbean.utils.MockGenerator
import io.github.taesk.lazymockbean.utils.SpringBeans
import org.springframework.test.context.TestContext
import org.springframework.util.ClassUtils
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import kotlin.reflect.KClass

object LazyMockDefinitionParser {
    private val EXCLUDE_BEAN_PACKAGE: List<String> = listOf("java.util", "java.lang")

    fun parse(testContext: TestContext): Set<LazyMockDefinition> {
        return testContext.testClass.declaredFields
            .filter { Annotations.hasLazyMockBeanAnotations(it) }
            .flatMap { mockingField ->
                val targetFieldType = mockingField.type
                val mock = MockGenerator.generate(mockingField, testContext, targetFieldType)
                val injectTargets = Annotations.getInjectTargets(mockingField)
                if (injectTargets.isNotEmpty()) {
                    parseSpecificTargets(injectTargets, testContext, mockingField, mock)
                } else {
                    parseAutowiredDependencyBeans(testContext, mockingField, mock)
                }
            }.toSet()
    }

    private fun parseSpecificTargets(
        injectTargets: Array<KClass<*>>,
        testContext: TestContext,
        mockingField: Field,
        mockObject: Any
    ): List<LazyMockDefinition> {
        return injectTargets.flatMap { generateDefinition(testContext, it.java, mockingField, mockObject) }
    }

    private fun generateDefinition(
        testContext: TestContext,
        target: Class<*>,
        mockingField: Field,
        mockObject: Any
    ): List<LazyMockDefinition> {
        return findMockingTarget(
            testContext = testContext,
            targetObject = target,
            targetFieldType = mockingField.type
        ).map { (targetBean, targetField) ->
            val originValue = targetField.getForce(targetBean)
            LazyMockDefinition(
                mockingField = mockingField,
                targetField = targetField,
                targetBean = targetBean,
                origin = originValue,
                mock = mockObject,
            )
        }
    }

    private fun findMockingTarget(
        testContext: TestContext,
        targetObject: Class<*>,
        targetFieldType: Class<*>
    ): List<LazyMockTarget> {
        val beans = SpringBeans.findAllBeansWithoutProxy(testContext, targetObject)
        return beans.map { bean ->
            val targetFieldInBean = ReflectionUtils.findField(bean::class.java, null, targetFieldType)
            requireNotNull(targetFieldInBean) { "not exist field [${targetFieldType.name}] in bean [${targetObject.simpleName}]" }
            LazyMockTarget(
                targetBean = bean,
                targetField = targetFieldInBean
            )
        }
    }

    private fun Field.getForce(parent: Any): Any {
        this.trySetAccessible()
        return this.get(parent)
    }

    private fun parseAutowiredDependencyBeans(
        testContext: TestContext,
        mockingField: Field,
        mockObject: Any,
    ): List<LazyMockDefinition> {
        val targetFieldType = mockingField.type
        val dependencyFinder = DependencyFinder()
        testContext.testClass.declaredFields
            .filter { it.isAnnotationPresent(LazyInjectMockBeans::class.java) }
            .forEach { dependencyFinder.push(it.type) }
        val mockDefinitions = mutableListOf<LazyMockDefinition>()
        while (!dependencyFinder.isEmpty()) {
            val depedentClazz = dependencyFinder.pop()
            val beans = SpringBeans.findAllBeansWithoutProxy(testContext, depedentClazz)
            for (bean in beans) {
                bean.javaClass.declaredFields
                    .filter { it.type == targetFieldType }
                    .flatMap { generateDefinition(testContext, depedentClazz, mockingField, mockObject) }
                    .let { mockDefinitions.addAll(it) }
                bean.javaClass.declaredFields
                    .filter { isValidateBeanType(it.type) }
                    .filter { SpringBeans.hasAnyBean(testContext, it.type) }
                    .filter { !dependencyFinder.hasAlradyFound(it.type) }
                    .forEach { dependencyFinder.push(it.type) }
            }
        }
        return mockDefinitions
    }

    private fun isValidateBeanType(type: Class<*>): Boolean {
        return !ClassUtils.isPrimitiveOrWrapper(type) &&
            !ClassUtils.isPrimitiveArray(type) &&
            !ClassUtils.isPrimitiveWrapperArray(type) &&
            type.`package`.toString() !in EXCLUDE_BEAN_PACKAGE
    }
}
