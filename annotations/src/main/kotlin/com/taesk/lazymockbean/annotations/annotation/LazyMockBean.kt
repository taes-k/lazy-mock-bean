package com.taesk.lazymockbean.annotations.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.TYPE, AnnotationTarget.PROPERTY)
annotation class LazyMockBean(
    val value: Array<KClass<*>>
)
