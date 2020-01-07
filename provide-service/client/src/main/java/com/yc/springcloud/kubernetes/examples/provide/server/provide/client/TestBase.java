package com.yc.springcloud.kubernetes.examples.provide.server.provide.client;

import com.yc.springcloud.kubernetes.examples.provide.server.TestDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
public interface TestBase {

    @GetMapping("/hello")
    TestDto test();
}
