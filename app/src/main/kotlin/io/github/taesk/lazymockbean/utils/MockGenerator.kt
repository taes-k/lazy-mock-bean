package io.github.taesk.lazymockbean.utils

import io.github.taesk.lazymockbean.annotation.LazySpyBean
import org.mockito.AdditionalAnswers
import org.mockito.Mockito
import org.springframework.aop.support.AopUtils
import org.springframework.test.context.TestContext
import java.lang.reflect.Field

object MockGenerator {
    fun generate(field: Field, testContext: TestContext, targetFieldType: Class<*>): Any {
        if (field.isAnnotationPresent(LazySpyBean::class.java)) {
            return getSpyMock(testContext, targetFieldType)
        }
        return Mockito.mock(targetFieldType)
    }

    private fun getSpyMock(testContext: TestContext, targetFieldType: Class<*>): Any {
        val spyTargetBean = SpringBeans.findBean(testContext, targetFieldType)
        return if (AopUtils.isAopProxy(spyTargetBean)) {
            Mockito.mock(targetFieldType, AdditionalAnswers.delegatesTo<Any>(spyTargetBean))
        } else {
            Mockito.spy(spyTargetBean)
        }
    }
}
