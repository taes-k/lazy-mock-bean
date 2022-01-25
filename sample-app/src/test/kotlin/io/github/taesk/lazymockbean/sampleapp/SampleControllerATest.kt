package io.github.taesk.lazymockbean.sampleapp

import io.github.taesk.lazymockbean.annotation.LazyMockBean
import io.github.taesk.lazymockbean.annotation.LazySpyBean
import io.github.taesk.lazymockbean.sampleapp.controller.SampleController
import io.github.taesk.lazymockbean.sampleapp.service.SampleServiceA
import io.github.taesk.lazymockbean.sampleapp.service.SampleServiceC
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SampleControllerATest {
    @LazyMockBean([SampleController::class])
    private lateinit var sampleServiceA: SampleServiceA

    @LazySpyBean([SampleController::class])
    private lateinit var sampleServiceC: SampleServiceC

    @Autowired
    private lateinit var sut: SampleController

    @Test
    fun getSampleA1() {
        // given
        val mockingResult = "MOCK_sample-A-1"
        given(sampleServiceA.getSample1()).willReturn(mockingResult)

        // when
        val result = sut.getSampleA1()

        // then
        then(result).isEqualTo(mockingResult)
    }

    @Test
    fun getSampleC1_withoutMocking() {
        // given
        val expectedResult = "sample-C-1"

        // when
        val result = sut.getSampleC1()

        // then
        then(result).isEqualTo(expectedResult)
    }

    @Test
    fun getSampleC1_withMocking() {
        // given
        val mockingResult = "MOCK_sample-C-1"
        given(sampleServiceC.getSample1()).willReturn(mockingResult)

        // when
        val result = sut.getSampleC1()

        // then
        then(result).isEqualTo(mockingResult)
    }
}
