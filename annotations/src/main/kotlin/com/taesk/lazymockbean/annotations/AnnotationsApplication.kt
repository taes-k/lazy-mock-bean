package com.taesk.lazymockbean.annotations

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AnnotationsApplication

fun main(args: Array<String>) {
    runApplication<AnnotationsApplication>(*args)
}
