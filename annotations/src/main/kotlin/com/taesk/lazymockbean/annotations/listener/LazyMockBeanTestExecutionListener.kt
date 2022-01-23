package com.taesk.lazymockbean.annotations.listener

import org.springframework.core.Ordered
import org.springframework.test.context.TestContext
import org.springframework.test.context.TestExecutionListener

class LazyMockBeanTestExecutionListener : TestExecutionListener, Ordered {
    override fun beforeTestClass(testContext: TestContext) {
        println("beforeTestClass")
    }

    override fun afterTestClass(testContext: TestContext) {
        println("afterTestClass")
    }

    override fun getOrder(): Int {
        return Integer.MAX_VALUE
    }
}
