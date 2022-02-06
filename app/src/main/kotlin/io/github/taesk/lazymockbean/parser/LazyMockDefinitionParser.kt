package io.github.taesk.lazymockbean.parser

import io.github.taesk.lazymockbean.data.DependencyFinder
import io.github.taesk.lazymockbean.data.LazyMockDefinition
import io.github.taesk.lazymockbean.data.LazyMockTarget
import io.github.taesk.lazymockbean.utils.Annotations
import io.github.taesk.lazymockbean.utils.MockGenerator
import io.github.taesk.lazymockbean.utils.SpringBeans
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestContext
import org.springframework.util.ClassUtils
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import kotlin.reflect.KClass

object LazyMockDefinitionParser {
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
        return injectTargets.map { generateDefinition(testContext, it.java, mockingField, mockObject) }
    }

    private fun generateDefinition(
        testContext: TestContext,
        target: Class<*>,
        mockingField: Field,
        mockObject: Any
    ): LazyMockDefinition {
        val (targetBean, targetField) = findMockingTarget(
            testContext = testContext,
            targetObject = target,
            targetFieldType = mockingField.type
        )
        val originValue = targetField.getForce(targetBean)
        return LazyMockDefinition(
            mockingField = mockingField,
            targetField = targetField,
            targetBean = targetBean,
            origin = originValue,
            mock = mockObject,
        )
    }

    private fun findMockingTarget(
        testContext: TestContext,
        targetObject: Class<*>,
        targetFieldType: Class<*>
    ): LazyMockTarget {
        val targetBean = SpringBeans.findBeanWithoutProxy(testContext, targetObject)
        val targetFieldInBean = ReflectionUtils.findField(targetBean::class.java, null, targetFieldType)
        requireNotNull(targetFieldInBean) { "not exist field [${targetFieldType.name}] in bean [${targetObject.simpleName}]" }

        return LazyMockTarget(
            targetBean = targetBean,
            targetField = targetFieldInBean
        )
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
            .filter { it.isAnnotationPresent(Autowired::class.java) }
            .forEach { dependencyFinder.push(it.type) }
        val mockDefinitions = mutableListOf<LazyMockDefinition>()
        while (!dependencyFinder.isEmpty()) {
            val depedentClazz = dependencyFinder.pop()
            depedentClazz.declaredFields
                .filter { it.type == targetFieldType }
                .map { generateDefinition(testContext, depedentClazz, mockingField, mockObject) }
                .let { mockDefinitions.addAll(it) }
            depedentClazz.declaredFields
                .filter { !ClassUtils.isPrimitiveOrWrapper(it.type) }
                .filter { SpringBeans.hasAnyBean(testContext, it.type) }
                .filter { !dependencyFinder.hasAlradyFound(it.type) }
                .forEach { dependencyFinder.push(it.type) }
        }
        return mockDefinitions
    }
}
