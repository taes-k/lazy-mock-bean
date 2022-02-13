package io.github.lazymockbean.sampleapp.service;

import org.springframework.stereotype.Service;

@Service
public class SampleServiceA {
    public String getSample1() {
        return "sample-A-1";
    }

    public String getSample2() {
        return "sample-A-2";
    }
}
