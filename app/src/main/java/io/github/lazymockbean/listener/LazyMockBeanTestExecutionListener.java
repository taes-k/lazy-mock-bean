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
    private static final String MOCKS_ATTRIBUTE_NAME = LazyMockBeanTestExecutionListener.class.getName() + ".mocks";

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

    @SuppressWarnings("unchecked")
    @Override
    public void beforeTestMethod(@NonNull TestContext testContext) throws IllegalAccessException {
        Set<LazyMockDefinition> definitions = (Set<LazyMockDefinition>) testContext.getAttribute(MOCKS_ATTRIBUTE_NAME);
        if (!CollectionUtils.isEmpty(definitions)) {
            injectFields(testContext, definitions);
        }
    }

    public void injectFields(TestContext testContext, Set<LazyMockDefinition> definitions) throws IllegalAccessException {
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

    @SuppressWarnings("unchecked")
    @Override
    public void afterTestClass(@NonNull TestContext testContext) throws IllegalAccessException {
        Set<LazyMockDefinition> definitions = (Set<LazyMockDefinition>) testContext.getAttribute(MOCKS_ATTRIBUTE_NAME);
        if (!CollectionUtils.isEmpty(definitions)) {
            revertFields(definitions);
            testContext.removeAttribute(MOCKS_ATTRIBUTE_NAME);
        }
    }

    private void revertFields(Set<LazyMockDefinition> definitions) throws IllegalAccessException {
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
