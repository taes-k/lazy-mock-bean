package com.taesk.lazymockbean.annotations.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
annotation class LazyMockBean(
    val value: Array<KClass<*>>
)
