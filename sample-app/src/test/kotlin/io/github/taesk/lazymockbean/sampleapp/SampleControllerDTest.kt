package io.github.taesk.lazymockbean.sampleapp

import io.github.taesk.lazymockbean.annotation.LazyMockBean
import io.github.taesk.lazymockbean.annotation.LazySpyBean
import io.github.taesk.lazymockbean.sampleapp.controller.SampleController
import io.github.taesk.lazymockbean.sampleapp.service.SampleServiceD1
import io.github.taesk.lazymockbean.sampleapp.service.SampleServiceD2
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SampleControllerDTest {
    @LazyMockBean // without inject target
    private lateinit var sampleServiceD1: SampleServiceD1

    @LazySpyBean // without inject target
    private lateinit var sampleServiceD2: SampleServiceD2

    @Autowired
    private lateinit var sut: SampleController

    @Test
    fun getSampleD1_1() {
        // given
        val mockingResult = "MOCK_sample-D-1-1"
        given(sampleServiceD1.getSample1()).willReturn(mockingResult)

        // when
        val result = sut.getSampleD1_1()

        // then
        then(result).isEqualTo(mockingResult)
    }

    @Test
    fun getSampleD1_2() {
        // given
        val mockingResult = "MOCK_sample-D-1-2"
        given(sampleServiceD1.getSample2()).willReturn(mockingResult)

        // when
        val result = sut.getSampleD1_2()

        // then
        then(result).isEqualTo(mockingResult)
    }

    @Test
    fun getSampleD2_1_withoutMocking() {
        // given
        val mockingResult = "sample-D-2-1"

        // when
        val result = sut.getSampleD2_1()

        // then
        then(result).isEqualTo(mockingResult)
    }

    @Test
    fun getSampleD2_2_withMocking() {
        // given
        val mockingResult = "MOCK_sample-D-2-2"
        given(sampleServiceD2.getSample2()).willReturn(mockingResult)

        // when
        val result = sut.getSampleD2_2()

        // then
        then(result).isEqualTo(mockingResult)
    }
}
