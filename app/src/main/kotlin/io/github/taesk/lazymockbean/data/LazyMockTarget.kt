package io.github.taesk.lazymockbean.data

import java.lang.reflect.Field

data class LazyMockTarget(
    val targetBean: Any,
    val targetField: Field
)
