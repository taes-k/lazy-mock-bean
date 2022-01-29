package io.github.taesk.lazymockbean.sampleapp.controller

import io.github.taesk.lazymockbean.sampleapp.service.ExampleService
import org.springframework.stereotype.Controller

@Controller
class ExampleController(
    private val exampleService: ExampleService
) {
    fun getExampleA1(): String {
        return exampleService.getExample1()
    }

    fun getExampleA2(): String {
        return exampleService.getExample2()
    }
}
