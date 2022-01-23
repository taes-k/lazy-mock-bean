package com.taesk.lazymockbean.sampleapp.controller

import com.taesk.lazymockbean.sampleapp.service.SampleServiceA
import com.taesk.lazymockbean.sampleapp.service.SampleServiceB
import org.springframework.stereotype.Controller

@Controller
class SampleController(
    val sampleServiceA: SampleServiceA,
    val sampleServiceB: SampleServiceB
) {
    fun getSampleA1(): String {
        return sampleServiceA.getSample1()
    }

    fun getSampleA2(): String {
        return sampleServiceA.getSample2()
    }

    fun getSampleB1(): String {
        return sampleServiceB.getSample1()
    }

    fun getSampleB2(): String {
        return sampleServiceB.getSample2()
    }
}
