package io.github.lazymockbean.utils;

import io.github.lazymockbean.annotation.LazySpyBean;
import lombok.experimental.UtilityClass;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.context.TestContext;

import java.lang.reflect.Field;

@UtilityClass
public class MockGenerator {
    public Object generate(Field field, TestContext testContext, Class<?> targetFieldType) {
        if (field.isAnnotationPresent(LazySpyBean.class)) {
            return getSpyMock(testContext, targetFieldType);
        }
        return Mockito.mock(targetFieldType);
    }

    private Object getSpyMock(TestContext testContext, Class<?> targetFieldType) {
        Object spyTargetBean = SpringBeans.findBean(testContext, targetFieldType);
        if (AopUtils.isAopProxy(spyTargetBean)) {
            return Mockito.mock(targetFieldType, AdditionalAnswers.delegatesTo(spyTargetBean));
        } else {
            return Mockito.spy(spyTargetBean);
        }
    }
}
