package io.github.taesk.lazymockbean.listener

import io.github.taesk.lazymockbean.data.LazyMockDefinition
import io.github.taesk.lazymockbean.parser.LazyMockDefinitionParser
import org.mockito.Mockito
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener

class LazyMockBeanTestExecutionListener : AbstractTestExecutionListener() {
    override fun beforeTestClass(testContext: TestContext) {
        initMockDefinitions(testContext)
    }

    private fun initMockDefinitions(testContext: TestContext) {
        val definitions = LazyMockDefinitionParser.parse(testContext)
        if (definitions.isNotEmpty()) {
            testContext.setAttribute(MOCKS_ATTRIBUTE_NAME, definitions)
        }
    }

    override fun prepareTestInstance(testContext: TestContext) {
        injectFields(testContext)
    }

    @Suppress("UNCHECKED_CAST")
    private fun injectFields(testContext: TestContext) {
        val definitions = testContext.getAttribute(MOCKS_ATTRIBUTE_NAME) as Set<LazyMockDefinition>?
        for (definition in definitions.orEmpty()) {
            Mockito.reset(definition.mock)

            val mockingField = definition.mockingField
            mockingField.trySetAccessible()
            mockingField.set(testContext.testInstance, definition.mock)

            val targetField = definition.targetField
            targetField.trySetAccessible()
            targetField.set(definition.targetBean, definition.mock)
        }
    }

    override fun afterTestClass(testContext: TestContext) {
        revertFields(testContext)
    }

    @Suppress("UNCHECKED_CAST")
    private fun revertFields(testContext: TestContext) {
        val definitions = testContext.getAttribute(MOCKS_ATTRIBUTE_NAME) as Set<LazyMockDefinition>?
        for (definition in definitions.orEmpty()) {
            val targetField = definition.targetField
            targetField.trySetAccessible()
            targetField.set(definition.targetBean, definition.origin)
        }
    }

    override fun getOrder(): Int {
        return Integer.MAX_VALUE
    }

    companion object {
        private const val MOCKS_ATTRIBUTE_NAME: String = "LAZY_MOCK_BEANS"
    }
}
