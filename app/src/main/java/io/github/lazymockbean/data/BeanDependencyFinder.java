package io.github.lazymockbean.data;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class BeanDependencyFinder {
    private final Set<Class<?>> visits = new HashSet<>();
    private final Stack<Class<?>> visitStack = new Stack<>();

    public void push(Class<?> clazz) {
        if (!hasVisited(clazz)) {
            visitStack.push(clazz);
            visits.add(clazz);
        }
    }

    private Boolean hasVisited(Class<?> clazz) {
        return visits.contains(clazz);
    }

    public Class<?> pop() {
        return visitStack.pop();
    }

    public Boolean hasMore() {
        return !visitStack.isEmpty();
    }
}
