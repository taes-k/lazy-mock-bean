package io.github.lazymockbean.data;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.Objects;

@Data
public class LazyMockDefinition {
    private final Field mockingField;
    private final Field targetField;
    private final Object targetBean;
    private final Object origin;
    private final Object mock;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LazyMockDefinition that = (LazyMockDefinition) o;
        return mockingField.equals(that.mockingField) && targetField.equals(that.targetField) && targetBean.equals(that.targetBean);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mockingField, targetField, targetBean);
    }
}
