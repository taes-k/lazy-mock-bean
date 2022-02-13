package io.github.lazymockbean.utils;

import io.github.lazymockbean.annotation.LazyMockBean;
import io.github.lazymockbean.annotation.LazySpyBean;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

@UtilityClass
public class Annotations {
    public Boolean hasLazyMockBeanAnotations(Field field) {
        return field.isAnnotationPresent(LazyMockBean.class) || field.isAnnotationPresent(LazySpyBean.class);
    }

    public Class<?>[] getInjectTargets(Field field) {
        if (!hasLazyMockBeanAnotations(field)) {
            return new Class[0];
        }

        if (field.isAnnotationPresent(LazySpyBean.class)) {
            return field.getAnnotation(LazySpyBean.class).value();
        }
        return field.getAnnotation(LazyMockBean.class).value();
    }
}
