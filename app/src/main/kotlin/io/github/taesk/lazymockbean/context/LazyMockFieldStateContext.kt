package io.github.taesk.lazymockbean.context

import io.github.taesk.lazymockbean.data.LazyMockDefinition

object LazyMockFieldStateContext {
    private val threadLocal = ThreadLocal<Set<LazyMockDefinition>>()

    fun addLocal(value: Set<LazyMockDefinition>) {
        val stateContextList = threadLocal.get()
        if (stateContextList.isNullOrEmpty()) {
            threadLocal.set(value)
        } else {
            threadLocal.set(stateContextList + value)
        }
    }

    fun getLocal(): Set<LazyMockDefinition>? {
        return threadLocal.get()
    }

    fun removeLocal() {
        return threadLocal.remove()
    }
}
