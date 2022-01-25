package io.github.taesk.lazymockbean.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
annotation class LazyMockBean(
    val value: Array<KClass<*>>
)
