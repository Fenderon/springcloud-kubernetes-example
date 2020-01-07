package com.yc.springcloud.kubernetes.examples.consumer;

import com.yc.springcloud.kubernetes.examples.provide.server.provide.client.TestBase;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("provide-service")
public interface TestClient extends TestBase {

}
