package io.github.taesk.lazymockbean.parser

import io.github.taesk.lazymockbean.annotation.LazyMockBean
import io.github.taesk.lazymockbean.data.LazyMockDefinition
import io.github.taesk.lazymockbean.parser.LazyFieldStateParser.Companion.findMockingTarget
import io.github.taesk.lazymockbean.parser.LazyFieldStateParser.Companion.getForce
import org.mockito.Mockito
import org.springframework.test.context.TestContext

object LazyMockFieldStateParser : LazyFieldStateParser {
    override fun parse(testContext: TestContext): Set<LazyMockDefinition> {
        return testContext.testClass.declaredFields
            .filter { it.isAnnotationPresent(LazyMockBean::class.java) }
            .flatMap { mockingField ->
                val targetFieldType = mockingField.type
                val injectTargets = mockingField.getAnnotation(LazyMockBean::class.java).value
                val mock = Mockito.mock(targetFieldType)

                injectTargets.map { injectTarget ->
                    val (targetBean, targetField) = findMockingTarget(
                        testContext = testContext,
                        targetObject = injectTarget.java,
                        targetFieldType = targetFieldType
                    )
                    val originValue = targetField.getForce(targetBean)
                    LazyMockDefinition(
                        mockingField = mockingField,
                        targetField = targetField,
                        targetBean = targetBean,
                        origin = originValue,
                        mock = mock,
                    )
                }
            }.toSet()
    }
}
