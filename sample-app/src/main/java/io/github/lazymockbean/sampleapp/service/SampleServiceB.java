package io.github.lazymockbean.sampleapp.service;

import org.springframework.stereotype.Service;

@Service
public class SampleServiceB {
    public String getSample1() {
        return "sample-B-1";
    }

    public String getSample2() {
        return "sample-B-2";
    }
}
