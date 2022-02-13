package io.github.lazymockbean.sampleapp.controller;

import io.github.lazymockbean.sampleapp.service.SampleServiceA;
import io.github.lazymockbean.sampleapp.service.SampleServiceB;
import io.github.lazymockbean.sampleapp.service.SampleServiceC;
import io.github.lazymockbean.sampleapp.service.SampleServiceD;
import org.springframework.stereotype.Controller;

@Controller
public class SampleController {
    private final SampleServiceA sampleServiceA;
    private final SampleServiceB sampleServiceB;
    private final SampleServiceC sampleServiceC;
    private final SampleServiceD sampleServiceD;

    public SampleController(
            SampleServiceA sampleServiceA, SampleServiceB sampleServiceB, SampleServiceC sampleServiceC, SampleServiceD sampleServiceD) {
        this.sampleServiceA = sampleServiceA;
        this.sampleServiceB = sampleServiceB;
        this.sampleServiceC = sampleServiceC;
        this.sampleServiceD = sampleServiceD;
    }

    public String getSampleA1() {
        return sampleServiceA.getSample1();
    }

    public String getSampleA2() {
        return sampleServiceA.getSample2();
    }

    public String getSampleB1() {
        return sampleServiceB.getSample1();
    }

    public String getSampleB2() {
        return sampleServiceB.getSample2();
    }

    public String getSampleC1() {
        return sampleServiceC.getSample1();
    }

    public String getSampleC2() {
        return sampleServiceC.getSample2();
    }

    public String getSampleD1_1() {
        return sampleServiceD.getSample1_1();
    }

    public String getSampleD1_2() {
        return sampleServiceD.getSample1_2();
    }

    public String getSampleD2_1() {
        return sampleServiceD.getSample2_1();
    }

    public String getSampleD2_2() {
        return sampleServiceD.getSample2_2();
    }

}
