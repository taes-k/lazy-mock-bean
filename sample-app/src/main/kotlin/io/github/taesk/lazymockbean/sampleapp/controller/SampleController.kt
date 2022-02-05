package io.github.taesk.lazymockbean.sampleapp.controller

import io.github.taesk.lazymockbean.sampleapp.service.SampleServiceA
import io.github.taesk.lazymockbean.sampleapp.service.SampleServiceB
import io.github.taesk.lazymockbean.sampleapp.service.SampleServiceC
import io.github.taesk.lazymockbean.sampleapp.service.SampleServiceD
import org.springframework.stereotype.Controller

@Controller
class SampleController(
    private val sampleServiceA: SampleServiceA,
    private val sampleServiceB: SampleServiceB,
    private val sampleServiceC: SampleServiceC,
    private val sampleServiceD: SampleServiceD
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

    fun getSampleD1_1(): String {
        return sampleServiceD.getSample1_1()
    }

    fun getSampleD1_2(): String {
        return sampleServiceD.getSample1_2()
    }

    fun getSampleD2_1(): String {
        return sampleServiceD.getSample2_1()
    }

    fun getSampleD2_2(): String {
        return sampleServiceD.getSample2_2()
    }
}
