package io.github.taesk.lazymockbean.parser

import io.github.taesk.lazymockbean.annotation.LazySpyBean
import io.github.taesk.lazymockbean.data.LazyMockFieldState
import io.github.taesk.lazymockbean.parser.LazyFieldStateParser.Companion.getForce
import org.mockito.Mockito
import org.springframework.test.context.TestContext

object LazySpyFieldStateParser : LazyFieldStateParser {
    override fun parse(testContext: TestContext): Set<LazyMockFieldState> {
        return testContext.testClass.declaredFields
            .filter { it.isAnnotationPresent(LazySpyBean::class.java) }
            .flatMap { testField ->
                val testFieldType = testField.type
                val injectTargets = testField.getAnnotation(LazySpyBean::class.java).value

                injectTargets.map { injectTarget ->
                    val (targetBean, targetField) = LazyFieldStateParser.getLazyMockTargetBean(
                        testContext = testContext,
                        targetObject = injectTarget.java,
                        targetFieldType = testFieldType
                    )
                    val originValue = targetField.getForce(targetBean)
                    LazyMockFieldState(
                        testField = testField,
                        field = targetField,
                        parents = targetBean,
                        origin = originValue,
                        mock = Mockito.spy(originValue),
                    )
                }
            }.toSet()
    }
}
