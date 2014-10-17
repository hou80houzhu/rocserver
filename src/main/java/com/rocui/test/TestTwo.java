package com.rocui.test;

public class TestTwo {

    public String two = "oooooooooooo";

    public String getTwo() {
        return this.two;
    }

    public String aaa(String cc) {
        System.out.println("----->>>" + cc);
        return this.two.toString();
    }

    public String aaa(int cc) {
        System.out.println("----->>><<<<" + cc);
        return this.two.toString();
    }

    public String aaa() {
        return this.two.toString();
    }
}
