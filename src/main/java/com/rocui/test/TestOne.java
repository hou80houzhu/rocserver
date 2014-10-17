package com.rocui.test;

public class TestOne extends TestTwo {

    private Object kk = "xxxxxxxxxxxx";

    public String getKk() {
        if (kk != null) {
            return kk.toString();
        } else {
            return "=er=";
        }
    }

    public String aa(String cc) {
        System.out.println("----->>>" + cc);
        return this.kk.toString();
    }

    public String aa(int cc) {
        System.out.println("----->>>" + cc);
        return this.kk.toString();
    }

    public String aa() {
        return this.kk.toString();
    }
}
