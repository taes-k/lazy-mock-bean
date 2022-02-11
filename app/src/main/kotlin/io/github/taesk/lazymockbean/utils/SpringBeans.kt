package io.github.taesk.lazymockbean.utils

import org.springframework.aop.framework.AopProxyUtils
import org.springframework.aop.support.AopUtils
import org.springframework.test.context.TestContext

object SpringBeans {
    fun hasAnyBean(
        testContext: TestContext,
        targetObject: Class<*>
    ): Boolean {
        val targetBeans = testContext.applicationContext.getBeansOfType(targetObject)
        return targetBeans.values.any()
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

    fun findAllBeansWithoutProxy(
        testContext: TestContext,
        targetObject: Class<*>
    ): List<Any> {
        return findAllBean(testContext, targetObject).map { unWrapProxy(it) }
    }

    private fun findAllBean(
        testContext: TestContext,
        targetObject: Class<*>
    ): List<Any> {
        val targetBeans = testContext.applicationContext.getBeansOfType(targetObject)
        return targetBeans.values.toList()
    }

    private fun unWrapProxy(targetBeanCandidate: Any): Any {
        return if (AopUtils.isAopProxy(targetBeanCandidate)) {
            checkNotNull(AopProxyUtils.getSingletonTarget(targetBeanCandidate))
        } else targetBeanCandidate
    }
}
