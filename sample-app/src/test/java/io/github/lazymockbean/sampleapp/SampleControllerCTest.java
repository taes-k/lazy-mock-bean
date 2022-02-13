package io.github.lazymockbean.sampleapp;

import io.github.lazymockbean.annotation.LazySpyBean;
import io.github.lazymockbean.sampleapp.controller.SampleController;
import io.github.lazymockbean.sampleapp.service.SampleServiceC;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class SampleControllerCTest {
    @LazySpyBean({SampleController.class})
    private SampleServiceC sampleServiceC;

    @Autowired
    private SampleController sut;

    @Test
    void getSampleC1_withoutMocking() {
        // given
        String expectedResult = "sample-C-1";

        // when
        String result = sut.getSampleC1();

        // then
        then(result).isEqualTo(expectedResult);
    }

    @Test
    void getSampleC1_withMocking() {
        // given
        String mockingResult = "MOCK_sample-C-1";
        given(sampleServiceC.getSample1()).willReturn(mockingResult);

        // when
        String result = sut.getSampleC1();

        // then
        then(result).isEqualTo(mockingResult);
    }
}
