package io.github.taesk.lazymockbean.utils

import io.github.taesk.lazymockbean.annotation.LazyMockBean
import io.github.taesk.lazymockbean.annotation.LazySpyBean
import java.lang.reflect.Field
import kotlin.reflect.KClass

object Annotations {
    fun hasLazyMockBeanAnotations(field: Field): Boolean {
        return field.isAnnotationPresent(LazyMockBean::class.java) || field.isAnnotationPresent(LazySpyBean::class.java)
    }

    fun getInjectTargets(field: Field): Array<KClass<*>> {
        if (hasLazyMockBeanAnotations(field)) {
            return emptyArray()
        }

        if (field.isAnnotationPresent(LazySpyBean::class.java)) {
            return field.getAnnotation(LazySpyBean::class.java).value
        }
        return field.getAnnotation(LazyMockBean::class.java).value
    }
}
