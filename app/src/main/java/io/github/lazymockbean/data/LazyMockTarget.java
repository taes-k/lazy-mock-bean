package io.github.lazymockbean.data;

import lombok.Data;

import java.lang.reflect.Field;

@Data
public class LazyMockTarget {
    private final Object targetBean;
    private final Field targetField;
}
