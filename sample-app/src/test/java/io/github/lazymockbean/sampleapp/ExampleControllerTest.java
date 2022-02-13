package io.github.lazymockbean.sampleapp;

import io.github.lazymockbean.annotation.LazyMockBean;
import io.github.lazymockbean.sampleapp.controller.ExampleController;
import io.github.lazymockbean.sampleapp.service.ExampleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class ExampleControllerTest {
    @LazyMockBean({ExampleController.class})
    private ExampleService exampleService;

    @Autowired
    private ExampleController sut;

    @Test
    void getExampleA1() {
        // given
        String mockingResult = "MOCK_example-A-1";
        given(exampleService.getExample1()).willReturn(mockingResult);

        // when
        String result = sut.getExampleA1();

        // then
        then(result).isEqualTo(mockingResult);
    }
}
