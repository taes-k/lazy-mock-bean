package io.github.taesk.lazymockbean.parser

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
import java.util.Stack
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
        return injectTargets.map {
            val (targetBean, targetField) = findMockingTarget(
                testContext = testContext,
                targetObject = it.java,
                targetFieldType = mockingField.type
            )
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
        val dependencyStack = Stack<Class<*>>()
        testContext.testClass.declaredFields
            .filter { it.isAnnotationPresent(Autowired::class.java) }
            .forEach { dependencyStack.push(it.type) }

        val mockDefinitions = mutableListOf<LazyMockDefinition>()
        while (dependencyStack.isNotEmpty()) {
            val depedentClazz = dependencyStack.pop()
            val definitions = depedentClazz.declaredFields
                .filter { it.type == targetFieldType }
                .map {
                    val (targetBean, targetField) = findMockingTarget(
                        testContext = testContext,
                        targetObject = depedentClazz,
                        targetFieldType = targetFieldType
                    )
                    val originValue = targetField.getForce(targetBean)
                    LazyMockDefinition(
                        mockingField = mockingField,
                        targetField = targetField,
                        targetBean = targetBean,
                        origin = originValue,
                        mock = mockObject,
                    )
                }
            mockDefinitions.addAll(definitions)
            depedentClazz.declaredFields
                .filter { !ClassUtils.isPrimitiveOrWrapper(it.type) }
                .forEach { dependencyStack.push(it.type) }
        }

        return mockDefinitions
    }
}
