package io.github.taesk.lazymockbean.parser

import io.github.taesk.lazymockbean.data.LazyMockDefinition
import io.github.taesk.lazymockbean.data.LazyMockTarget
import org.springframework.aop.framework.AopProxyUtils
import org.springframework.aop.support.AopUtils
import org.springframework.test.context.TestContext
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field

interface LazyFieldStateParser {
    fun parse(testContext: TestContext): Set<LazyMockDefinition>

    companion object {
        fun Field.getForce(parent: Any): Any {
            this.trySetAccessible()
            return this.get(parent)
        }

        fun findMockingTarget(
            testContext: TestContext,
            targetObject: Class<*>,
            targetFieldType: Class<*>
        ): LazyMockTarget {
            val targetBean = findBeanWithoutProxy(testContext, targetObject)
            val targetFieldInBean = ReflectionUtils.findField(targetBean::class.java, null, targetFieldType)
            requireNotNull(targetFieldInBean) { "not exist field [${targetFieldType.name}] in bean [${targetObject.simpleName}]" }

            return LazyMockTarget(
                targetBean = targetBean,
                targetField = targetFieldInBean
            )
        }

        fun findBean(
            testContext: TestContext,
            targetObject: Class<*>
        ): Any {
            val targetBeans = testContext.applicationContext.getBeansOfType(targetObject)
            require(targetBeans.isNotEmpty()) { "not exist bean [${targetObject.simpleName}] in beanFactory" }
            require(targetBeans.size == 1) { "more than 1 bean [${targetObject.simpleName}] in beanFactory" }

            return targetBeans.values.first()
        }

        private fun findBeanWithoutProxy(
            testContext: TestContext,
            targetObject: Class<*>
        ): Any {
            return unWrapProxy(findBean(testContext, targetObject))
        }

        private fun unWrapProxy(targetBeanCandidate: Any): Any {
            return if (AopUtils.isAopProxy(targetBeanCandidate)) {
                checkNotNull(AopProxyUtils.getSingletonTarget(targetBeanCandidate))
            } else targetBeanCandidate
        }
    }
}
