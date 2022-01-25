package io.github.taesk.lazymockbean.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
annotation class LazySpyBean(
    val value: Array<KClass<*>>
)
