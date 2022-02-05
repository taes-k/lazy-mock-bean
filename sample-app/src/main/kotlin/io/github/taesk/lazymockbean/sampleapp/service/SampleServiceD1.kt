package io.github.taesk.lazymockbean.sampleapp.service

import org.springframework.stereotype.Service

@Service
class SampleServiceD1 {
    fun getSample1(): String {
        return "sample-D-1-1"
    }

    fun getSample2(): String {
        return "sample-D-1-2"
    }
}
