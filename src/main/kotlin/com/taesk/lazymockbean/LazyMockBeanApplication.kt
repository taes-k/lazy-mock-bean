package com.taesk.lazymockbean

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LazyMockBeanApplication

fun main(args: Array<String>) {
	runApplication<LazyMockBeanApplication>(*args)
}
