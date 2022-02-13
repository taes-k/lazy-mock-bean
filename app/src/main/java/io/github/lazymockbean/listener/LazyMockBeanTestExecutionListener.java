package io.github.lazymockbean.listener;

import io.github.lazymockbean.data.LazyMockDefinition;
import io.github.lazymockbean.parser.LazyMockDefinitionParser;
import org.mockito.Mockito;
import org.springframework.lang.NonNull;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Set;

public class LazyMockBeanTestExecutionListener extends AbstractTestExecutionListener {
    private static final String MOCKS_ATTRIBUTE_NAME = "LAZY_MOCK_BEANS";

    @Override
    public void beforeTestClass(@NonNull TestContext testContext) {
        initMockDefinitions(testContext);
    }

    private void initMockDefinitions(TestContext testContext) {
        Set<LazyMockDefinition> definitions = LazyMockDefinitionParser.parse(testContext);
        if (!CollectionUtils.isEmpty(definitions)) {
            testContext.setAttribute(MOCKS_ATTRIBUTE_NAME, definitions);
        }
    }

    @Override
    public void prepareTestInstance(@NonNull TestContext testContext) throws IllegalAccessException {
        injectFields(testContext);
    }

    @SuppressWarnings("unchecked")
    public void injectFields(TestContext testContext) throws IllegalAccessException {
        Set<LazyMockDefinition> definitions = (Set<LazyMockDefinition>) testContext.getAttribute(MOCKS_ATTRIBUTE_NAME);
        if (CollectionUtils.isEmpty(definitions)) {
            return;
        }

        for (LazyMockDefinition definition : definitions) {
            Mockito.reset(definition.getMock());

            Field mockingField = definition.getMockingField();
            mockingField.trySetAccessible();
            mockingField.set(testContext.getTestInstance(), definition.getMock());

            Field targetField = definition.getTargetField();
            targetField.trySetAccessible();
            targetField.set(definition.getTargetBean(), definition.getMock());
        }
    }

    @Override
    public void afterTestClass(@NonNull TestContext testContext) throws IllegalAccessException {
        revertFields(testContext);
    }

    @SuppressWarnings("unchecked")
    private void revertFields(TestContext testContext) throws IllegalAccessException {
        Set<LazyMockDefinition> definitions = (Set<LazyMockDefinition>) testContext.getAttribute(MOCKS_ATTRIBUTE_NAME);
        if (CollectionUtils.isEmpty(definitions)) {
            return;
        }

        for (LazyMockDefinition definition : definitions) {
            Field targetField = definition.getTargetField();
            targetField.trySetAccessible();
            targetField.set(definition.getTargetBean(), definition.getOrigin());
        }
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
