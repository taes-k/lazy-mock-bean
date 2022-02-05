package io.github.taesk.lazymockbean.sampleapp.service

import org.springframework.stereotype.Service

@Service
class SampleServiceD2 {
    fun getSample1(): String {
        return "sample-D-2-1"
    }

    fun getSample2(): String {
        return "sample-D-2-2"
    }
}
