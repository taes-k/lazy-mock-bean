package com.taesk.lazymockbean.sampleapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SampleAppApplication

fun main(args: Array<String>) {
    runApplication<SampleAppApplication>(*args)
}
