package io.github.lazymockbean.sampleapp.service;

import org.springframework.stereotype.Service;

@Service
public class SampleServiceD {
    private final SampleServiceD1 sampleServiceD1;
    private final SampleServiceD2 sampleServiceD2;

    public SampleServiceD(SampleServiceD1 sampleServiceD1, SampleServiceD2 sampleServiceD2) {
        this.sampleServiceD1 = sampleServiceD1;
        this.sampleServiceD2 = sampleServiceD2;
    }

    public String getSample1_1() {
        return sampleServiceD1.getSample1();
    }

    public String getSample1_2() {
        return sampleServiceD1.getSample2();
    }

    public String getSample2_1() {
        return sampleServiceD2.getSample1();
    }

    public String getSample2_2() {
        return sampleServiceD2.getSample2();
    }
}
