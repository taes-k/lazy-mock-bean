package io.github.lazymockbean.sampleapp.controller;

import io.github.lazymockbean.sampleapp.service.ExampleService;
import org.springframework.stereotype.Controller;

@Controller
public class ExampleController {
    private final ExampleService exampleService;

    public ExampleController(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    public String getExampleA1() {
        return exampleService.getExample1();
    }

    public String getExampleA2() {
        return exampleService.getExample2();
    }
}
