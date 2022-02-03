package io.github.taesk.lazymockbean.parser

import io.github.taesk.lazymockbean.annotation.LazySpyBean
import io.github.taesk.lazymockbean.data.LazyMockDefinition
import io.github.taesk.lazymockbean.parser.LazyFieldStateParser.Companion.findBean
import io.github.taesk.lazymockbean.parser.LazyFieldStateParser.Companion.getForce
import org.mockito.AdditionalAnswers.delegatesTo
import org.mockito.Mockito
import org.springframework.aop.support.AopUtils
import org.springframework.test.context.TestContext

object LazySpyFieldStateParser : LazyFieldStateParser {
    override fun parse(testContext: TestContext): Set<LazyMockDefinition> {
        return testContext.testClass.declaredFields
            .filter { it.isAnnotationPresent(LazySpyBean::class.java) }
            .flatMap { mockingField ->
                val targetFieldType = mockingField.type
                val injectTargets = mockingField.getAnnotation(LazySpyBean::class.java).value
                val spyTargetBean = findBean(testContext, targetFieldType)
                val spy =
                    if (AopUtils.isAopProxy(spyTargetBean)) {
                        Mockito.mock(targetFieldType, delegatesTo<Any>(spyTargetBean))
                    } else {
                        Mockito.spy(spyTargetBean)
                    }

                injectTargets.map { injectTarget ->
                    val (targetBean, targetField) = LazyFieldStateParser.findMockingTarget(
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
                        mock = spy,
                    )
                }
            }.toSet()
    }
}
