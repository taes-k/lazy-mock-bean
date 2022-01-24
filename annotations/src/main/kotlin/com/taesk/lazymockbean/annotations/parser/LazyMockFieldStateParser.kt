package com.taesk.lazymockbean.annotations.parser

import com.taesk.lazymockbean.annotations.annotation.LazyMockBean
import com.taesk.lazymockbean.annotations.data.LazyMockFieldState
import org.mockito.Mockito
import org.springframework.test.context.TestContext
import org.springframework.util.ReflectionUtils

object LazyMockFieldStateParser {
    fun parse(testContext: TestContext): Set<LazyMockFieldState> {
        return testContext.testClass.declaredFields
            .filter { it.isAnnotationPresent(LazyMockBean::class.java) }
            .flatMap { testField ->
                val testFieldType = testField.type
                val injectTargets = testField.getAnnotation(LazyMockBean::class.java).value
                injectTargets.map { injectTarget ->
                    val injectTargetBeans = testContext.applicationContext.getBeansOfType(injectTarget.java)
                    require(injectTargetBeans.isNotEmpty()) { "not exist bean [${injectTarget.simpleName}] in beanFactory" }
                    require(injectTargetBeans.size == 1) { "more than 1 bean [${injectTarget.simpleName}] in beanFactory" }

                    val injectTargetBean = injectTargetBeans.values.first()
                    val targetFieldInBean = ReflectionUtils.findField(injectTargetBean::class.java, null, testFieldType)
                    requireNotNull(targetFieldInBean) { "not exist field [${testFieldType.name}] in bean [${injectTarget.simpleName}]" }
                    targetFieldInBean.trySetAccessible()

                    val originValue = targetFieldInBean.get(injectTargetBean)
                    LazyMockFieldState(
                        testField = testField,
                        field = targetFieldInBean,
                        parents = injectTargetBean,
                        origin = originValue,
                        mock = Mockito.mock(testFieldType),
                    )
                }
            }.toSet()
    }
}
