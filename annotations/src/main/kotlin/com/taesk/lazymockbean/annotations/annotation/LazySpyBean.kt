package com.taesk.lazymockbean.annotations.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
annotation class LazySpyBean(
    val value: Array<KClass<*>>
)
