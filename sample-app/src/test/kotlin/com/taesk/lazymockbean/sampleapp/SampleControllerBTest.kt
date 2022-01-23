package com.taesk.lazymockbean.sampleapp

import com.taesk.lazymockbean.annotations.annotation.LazyMockBean
import com.taesk.lazymockbean.sampleapp.controller.SampleController
import com.taesk.lazymockbean.sampleapp.service.SampleServiceB
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SampleControllerBTest {
    @LazyMockBean([SampleController::class])
    private lateinit var sampleServiceB: SampleServiceB

    @Autowired
    private lateinit var sut: SampleController

    @Test
    fun getSampleA1() {
        // given
        val expectedResult = "sample-A-1"

        // when
        val result = sut.getSampleA1()

        // then
        then(result).isEqualTo(expectedResult)
    }

    @Test
    fun getSampleB1() {
        // given
        val mockingResult = "MOCK_sample-B-1"
        given(sampleServiceB.getSample1()).willReturn(mockingResult)

        // when
        val result = sut.getSampleB1()

        // then
        then(result).isEqualTo(mockingResult)
    }
}
