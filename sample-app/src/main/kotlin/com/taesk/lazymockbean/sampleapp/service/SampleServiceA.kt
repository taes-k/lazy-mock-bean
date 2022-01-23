package com.taesk.lazymockbean.sampleapp.service

import org.springframework.stereotype.Service

@Service
class SampleServiceA {
    fun getSample1(): String {
        return "sample-A-1"
    }

    fun getSample2(): String {
        return "sample-A-2"
    }
}
