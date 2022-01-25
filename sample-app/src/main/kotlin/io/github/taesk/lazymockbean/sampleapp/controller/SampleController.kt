package io.github.taesk.lazymockbean.sampleapp.controller

import io.github.taesk.lazymockbean.sampleapp.service.SampleServiceA
import io.github.taesk.lazymockbean.sampleapp.service.SampleServiceB
import io.github.taesk.lazymockbean.sampleapp.service.SampleServiceC
import org.springframework.stereotype.Controller

@Controller
class SampleController(
    val sampleServiceA: SampleServiceA,
    val sampleServiceB: SampleServiceB,
    val sampleServiceC: SampleServiceC
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

    fun getSampleC1(): String {
        return sampleServiceC.getSample1()
    }

    fun getSampleC2(): String {
        return sampleServiceC.getSample2()
    }
}
