package io.github.taesk.lazymockbean.sampleapp.service

import org.springframework.stereotype.Service

@Service
class ExampleServiceA : ExampleService {
    override fun getExample1(): String {
        return "example-A-1"
    }

    override fun getExample2(): String {
        return "example-A-2"
    }
}
