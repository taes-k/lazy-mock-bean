package io.github.taesk.lazymockbean.sampleapp

import io.github.taesk.lazymockbean.annotation.LazyMockBean
import io.github.taesk.lazymockbean.sampleapp.controller.ExampleController
import io.github.taesk.lazymockbean.sampleapp.service.ExampleService
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ExampleControllerTest {
    @LazyMockBean([ExampleController::class])
    private lateinit var exampleService: ExampleService

    @Autowired
    private lateinit var sut: ExampleController

    @Test
    fun getExampleA1() {
        // given
        val mockingResult = "MOCK_example-A-1"
        given(exampleService.getExample1()).willReturn(mockingResult)

        // when
        val result = sut.getExampleA1()

        // then
        then(result).isEqualTo(mockingResult)
    }
}
