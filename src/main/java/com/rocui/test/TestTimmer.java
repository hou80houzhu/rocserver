package com.rocui.test;

import com.rocui.timmer.JtimmerTask;

public class TestTimmer extends JtimmerTask {

    public TestTimmer() {
        this.setCircle(5);
        this.setDelay(1);
        this.setType(JtimmerTask.TIMMER_TYPE_CIRCLE);
    }

    @Override
    public void excute() {
        System.out.println("I am a circle timmer...");
    }

    public String xx(String a, String b) {
        return a + b;
    }

}
