package io.github.lazymockbean.sampleapp;

import io.github.lazymockbean.annotation.LazyInjectMockBeans;
import io.github.lazymockbean.annotation.LazyMockBean;
import io.github.lazymockbean.annotation.LazySpyBean;
import io.github.lazymockbean.sampleapp.controller.SampleController;
import io.github.lazymockbean.sampleapp.service.SampleServiceD1;
import io.github.lazymockbean.sampleapp.service.SampleServiceD2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class SampleControllerDTest {
    @LazyMockBean
    private SampleServiceD1 sampleServiceD1;

    @LazySpyBean
    private SampleServiceD2 sampleServiceD2;

    @LazyInjectMockBeans
    @Autowired
    private SampleController sut;

    @Test
    void getSampleD1_1() {
        // given
        String mockingResult = "MOCK_sample-D-1-1";
        given(sampleServiceD1.getSample1()).willReturn(mockingResult);

        // when
        String result = sut.getSampleD1_1();

        // then
        then(result).isEqualTo(mockingResult);
    }

    @Test
    void getSampleD1_2() {
        // given
        String mockingResult = "MOCK_sample-D-1-2";
        given(sampleServiceD1.getSample2()).willReturn(mockingResult);

        // when
        String result = sut.getSampleD1_2();

        // then
        then(result).isEqualTo(mockingResult);
    }

    @Test
    void getSampleD2_1_withoutMocking() {
        // given
        String mockingResult = "sample-D-2-1";

        // when
        String result = sut.getSampleD2_1();

        // then
        then(result).isEqualTo(mockingResult);
    }

    @Test
    void getSampleD2_2_withMocking() {
        // given
        String mockingResult = "MOCK_sample-D-2-2";
        given(sampleServiceD2.getSample2()).willReturn(mockingResult);

        // when
        String result = sut.getSampleD2_2();

        // then
        then(result).isEqualTo(mockingResult);
    }
}
