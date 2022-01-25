package io.github.taesk.lazymockbean.data

import java.lang.reflect.Field
import java.util.Objects

data class LazyMockFieldState(
    val testField: Field,
    val field: Field,
    val parents: Any,
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

        return testField === other.testField &&
            field === other.field &&
            parents === other.parents
    }

    override fun hashCode(): Int {
        return Objects.hash(testField, field, parents)
    }
}
