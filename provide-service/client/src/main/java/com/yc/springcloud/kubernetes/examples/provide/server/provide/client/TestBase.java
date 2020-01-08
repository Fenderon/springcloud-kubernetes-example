package com.yc.springcloud.kubernetes.examples.provide.server.provide.client;

import com.yc.springcloud.kubernetes.examples.provide.server.TestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Api("Test Controller")
@RequestMapping("/api")
public interface TestBase {

    @ApiOperation("test method")
    @ApiResponse(code = 200, message = "test method", response = TestDto.class)
    @GetMapping("/hello")
    TestDto test();
}
