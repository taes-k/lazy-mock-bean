package io.github.taesk.lazymockbean.parser

import io.github.taesk.lazymockbean.annotation.LazyMockBean
import io.github.taesk.lazymockbean.data.LazyMockFieldState
import io.github.taesk.lazymockbean.parser.LazyFieldStateParser.Companion.getForce
import io.github.taesk.lazymockbean.parser.LazyFieldStateParser.Companion.getLazyMockTargetBean
import org.mockito.Mockito
import org.springframework.test.context.TestContext

object LazyMockFieldStateParser : LazyFieldStateParser {
    override fun parse(testContext: TestContext): Set<LazyMockFieldState> {
        return testContext.testClass.declaredFields
            .filter { it.isAnnotationPresent(LazyMockBean::class.java) }
            .flatMap { testField ->
                val testFieldType = testField.type
                val injectTargets = testField.getAnnotation(LazyMockBean::class.java).value
                val mockBean = Mockito.mock(testFieldType)

                injectTargets.map { injectTarget ->
                    val (targetBean, targetField) = getLazyMockTargetBean(
                        testContext = testContext,
                        targetObject = injectTarget.java,
                        targetFieldType = testFieldType
                    )
                    val originValue = targetField.getForce(targetBean)
                    LazyMockFieldState(
                        mockingField = testField,
                        targetField = targetField,
                        targetBean = targetBean,
                        origin = originValue,
                        mock = mockBean,
                    )
                }
            }.toSet()
    }
}
