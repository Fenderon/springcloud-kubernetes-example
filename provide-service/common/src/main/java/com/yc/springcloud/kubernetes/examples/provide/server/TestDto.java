package com.yc.springcloud.kubernetes.examples.provide.server;



public class TestDto {

    private String str;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return "TestDto{" +
                "str='" + str + '\'' +
                '}';
    }
}
