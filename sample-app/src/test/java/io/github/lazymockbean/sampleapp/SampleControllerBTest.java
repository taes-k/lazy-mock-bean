package io.github.lazymockbean.sampleapp;

import io.github.lazymockbean.annotation.LazyMockBean;
import io.github.lazymockbean.sampleapp.controller.SampleController;
import io.github.lazymockbean.sampleapp.service.SampleServiceB;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class SampleControllerBTest {
    @LazyMockBean({SampleController.class})
    private SampleServiceB sampleServiceB;

    @Autowired
    private SampleController sut;

    @Test
    void getSampleA1() {
        // given
        String expectedResult = "sample-A-1";

        // when
        String result = sut.getSampleA1();

        // then
        then(result).isEqualTo(expectedResult);
    }

    @Test
    void getSampleB1() {
        // given
        String mockingResult = "MOCK_sample-B-1";
        given(sampleServiceB.getSample1()).willReturn(mockingResult);

        // when
        String result = sut.getSampleB1();

        // then
        then(result).isEqualTo(mockingResult);
    }
}
