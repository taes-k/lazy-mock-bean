package io.github.lazymockbean.sampleapp.service;

import org.springframework.stereotype.Service;

@Service
public class SampleServiceC {
    public String getSample1() {
        return "sample-C-1";
    }

    public String getSample2() {
        return "sample-C-2";
    }
}
