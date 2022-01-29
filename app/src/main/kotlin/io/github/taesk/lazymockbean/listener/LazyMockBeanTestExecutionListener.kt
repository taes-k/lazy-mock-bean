package io.github.taesk.lazymockbean.listener

import io.github.taesk.lazymockbean.context.LazyMockFieldStateContext
import io.github.taesk.lazymockbean.parser.LazyMockFieldStateParser
import io.github.taesk.lazymockbean.parser.LazySpyFieldStateParser
import org.mockito.Mockito
import org.springframework.core.Ordered
import org.springframework.test.context.TestContext
import org.springframework.test.context.TestExecutionListener

class LazyMockBeanTestExecutionListener : TestExecutionListener, Ordered {
    override fun beforeTestClass(testContext: TestContext) {
        val lazyMockFieldStates = LazyMockFieldStateParser.parse(testContext)
        LazyMockFieldStateContext.addLocal(lazyMockFieldStates)

        val lazySpyFieldStates = LazySpyFieldStateParser.parse(testContext)
        LazyMockFieldStateContext.addLocal(lazySpyFieldStates)
        super.beforeTestClass(testContext)
    }

    override fun prepareTestInstance(testContext: TestContext) {
        setLazyMockField(testContext)
        super.prepareTestInstance(testContext)
    }

    private fun setLazyMockField(testContext: TestContext) {
        val stateContextList = LazyMockFieldStateContext.getLocal()
        for (lazyMockFieldState in stateContextList) {
            val testField = lazyMockFieldState.testField
            Mockito.reset(lazyMockFieldState.mock)
            testField.trySetAccessible()
            testField.set(testContext.testInstance, lazyMockFieldState.mock)

            val targetField = lazyMockFieldState.field
            targetField.trySetAccessible()
            targetField.set(lazyMockFieldState.parents, lazyMockFieldState.mock)
        }
    }

    override fun afterTestClass(testContext: TestContext) {
        rollbackLazyMockField()
        super.afterTestClass(testContext)
    }

    private fun rollbackLazyMockField() {
        val stateContextList = LazyMockFieldStateContext.getLocal()
        for (lazyMockFieldState in stateContextList) {
            val targetField = lazyMockFieldState.field
            targetField.trySetAccessible()
            targetField.set(lazyMockFieldState.parents, lazyMockFieldState.origin)
        }
        LazyMockFieldStateContext.removeLocal()
    }

    override fun getOrder(): Int {
        return Integer.MAX_VALUE
    }
}
