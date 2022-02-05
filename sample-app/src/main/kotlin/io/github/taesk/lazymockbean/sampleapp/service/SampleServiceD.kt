package io.github.taesk.lazymockbean.sampleapp.service

import org.springframework.stereotype.Service

@Service
class SampleServiceD(
    private val sampleServiceD1: SampleServiceD1,
    private val sampleServiceD2: SampleServiceD2
) {
    fun getSample1_1(): String {
        return sampleServiceD1.getSample1()
    }

    fun getSample1_2(): String {
        return sampleServiceD1.getSample2()
    }

    fun getSample2_1(): String {
        return sampleServiceD2.getSample1()
    }

    fun getSample2_2(): String {
        return sampleServiceD2.getSample2()
    }
}
