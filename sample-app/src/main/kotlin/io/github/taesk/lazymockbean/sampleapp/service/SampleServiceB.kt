package io.github.taesk.lazymockbean.sampleapp.service

import org.springframework.stereotype.Service

@Service
class SampleServiceB {
    fun getSample1(): String {
        return "sample-B-1"
    }

    fun getSample2(): String {
        return "sample-B-2"
    }
}
