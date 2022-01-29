package io.github.taesk.lazymockbean.parser

import io.github.taesk.lazymockbean.data.LazyMockFieldState
import io.github.taesk.lazymockbean.data.LazyMockTarget
import org.springframework.aop.framework.AopProxyUtils
import org.springframework.aop.support.AopUtils
import org.springframework.test.context.TestContext
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field

interface LazyFieldStateParser {
    fun parse(testContext: TestContext): Set<LazyMockFieldState>

    companion object {
        fun Field.getForce(parent: Any): Any {
            this.trySetAccessible()
            return this.get(parent)
        }

        fun getLazyMockTargetBean(
            testContext: TestContext,
            targetObject: Class<*>,
            targetFieldType: Class<*>
        ): LazyMockTarget {
            val targetBeans = testContext.applicationContext.getBeansOfType(targetObject)
            require(targetBeans.isNotEmpty()) { "not exist bean [${targetObject.simpleName}] in beanFactory" }
            require(targetBeans.size == 1) { "more than 1 bean [${targetObject.simpleName}] in beanFactory" }

            val targetBeanCandidate = targetBeans.values.first()
            val targetBean =
                if (AopUtils.isAopProxy(targetBeanCandidate)) {
                    AopProxyUtils.getSingletonTarget(targetBeanCandidate)
                } else targetBeanCandidate

            val targetFieldInBean = ReflectionUtils.findField(targetBean::class.java, null, targetFieldType)
            requireNotNull(targetFieldInBean) { "not exist field [${targetFieldType.name}] in bean [${targetObject.simpleName}]" }

            return LazyMockTarget(
                targetBean = targetBean,
                targetField = targetFieldInBean
            )
        }
    }
}
