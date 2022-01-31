package io.github.taesk.lazymockbean.data

import java.lang.reflect.Field
import java.util.Objects

data class LazyMockFieldState(
    val mockingField: Field,
    val targetField: Field,
    val targetBean: Any,
    val origin: Any,
    val mock: Any,
) {
    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        if (other !is LazyMockFieldState) {
            return false
        }

        return mockingField === other.mockingField &&
            targetField === other.targetField &&
            targetBean === other.targetBean
    }

    override fun hashCode(): Int {
        return Objects.hash(mockingField, targetField, targetBean)
    }
}
