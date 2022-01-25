package io.github.taesk.lazymockbean.sampleapp.service

import org.springframework.stereotype.Service

@Service
class SampleServiceC {
    fun getSample1(): String {
        return "sample-C-1"
    }

    fun getSample2(): String {
        return "sample-C-2"
    }
}
