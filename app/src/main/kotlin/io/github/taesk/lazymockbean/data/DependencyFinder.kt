package io.github.taesk.lazymockbean.data

import java.util.Stack

class DependencyFinder() {
    private val dependencyChecker: HashSet<Class<*>> = hashSetOf()
    private val dependencyStack: Stack<Class<*>> = Stack<Class<*>>()

    fun push(clazz: Class<*>) {
        dependencyStack.push(clazz)
        dependencyChecker.add(clazz)
    }

    fun isEmpty(): Boolean {
        return dependencyStack.isEmpty()
    }

    fun pop(): Class<*> {
        return dependencyStack.pop()
    }

    fun hasAlradyFound(clazz: Class<*>): Boolean {
        return clazz in dependencyChecker
    }
}
