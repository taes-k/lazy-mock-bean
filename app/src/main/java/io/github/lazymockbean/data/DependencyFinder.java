package io.github.lazymockbean.data;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class DependencyFinder {
    private final Set<Class<?>> dependencyChecker = new HashSet<>();
    private final Stack<Class<?>> dependencyStack = new Stack<>();

    public void push(Class<?> clazz) {
        dependencyStack.push(clazz);
        dependencyChecker.add(clazz);
    }

    public Boolean isEmpty() {
        return dependencyStack.isEmpty();
    }

    public Class<?> pop() {
        return dependencyStack.pop();
    }

    public Boolean hasAlradyFound(Class<?> clazz) {
        return dependencyChecker.contains(clazz);
    }
}
