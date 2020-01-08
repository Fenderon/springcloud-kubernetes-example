package com.yc.springcloud.kubernetes.examples.provide.server;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("TestDto")
public class TestDto {

    @ApiModelProperty("str字符串")
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
