/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yc.springcloud.kubernetes.examples.consumer;

import com.yc.springcloud.kubernetes.examples.common.base.BaseController;
import com.yc.springcloud.kubernetes.examples.provide.server.TestDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController extends BaseController {

    private static final Log log = LogFactory.getLog(HelloController.class);

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private TestClient testClient;

    @RequestMapping("/")
    public TestDto hello() {
        return testClient.test();
    }

    @RequestMapping("/services")
    public List<String> services() {
        return this.discoveryClient.getServices();
    }

}
