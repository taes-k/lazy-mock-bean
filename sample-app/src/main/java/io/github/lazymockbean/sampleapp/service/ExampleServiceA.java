package io.github.lazymockbean.sampleapp.service;

import org.springframework.stereotype.Service;

@Service
public class ExampleServiceA implements ExampleService {
    @Override
    public String getExample1() {
        return "example-A-1";
    }

    @Override
    public String getExample2() {
        return "example-A-2";
    }
}
