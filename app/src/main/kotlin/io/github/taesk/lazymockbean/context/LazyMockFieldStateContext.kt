package io.github.taesk.lazymockbean.context

import io.github.taesk.lazymockbean.data.LazyMockFieldState

object LazyMockFieldStateContext {
    private val threadLocal = ThreadLocal<Set<LazyMockFieldState>>()

    fun addLocal(value: Set<LazyMockFieldState>) {
        val stateContextList = threadLocal.get()
        if (stateContextList.isNullOrEmpty()) {
            threadLocal.set(value)
        } else {
            threadLocal.set(stateContextList + value)
        }
    }

    fun getLocal(): Set<LazyMockFieldState> {
        return threadLocal.get()
    }

    fun removeLocal() {
        return threadLocal.remove()
    }
}