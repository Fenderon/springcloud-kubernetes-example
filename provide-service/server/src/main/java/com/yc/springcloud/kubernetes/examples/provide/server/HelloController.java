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

package com.yc.springcloud.kubernetes.examples.provide.server;

import com.yc.springcloud.kubernetes.examples.common.base.BaseController;
import com.yc.springcloud.kubernetes.examples.provide.server.provide.client.TestBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description
 *
 * @author yangchuan
 * @version 1.0
 * @createDate 2020/1/5
 **/
@RestController
public class HelloController extends BaseController implements TestBase {

    private static final Log log = LogFactory.getLog(HelloController.class);

    @Autowired
    private DiscoveryClient discoveryClient;


    @RequestMapping("/services")
    public List<String> services() {
        return this.discoveryClient.getServices();
    }

    @Override
    public TestDto test() {
        TestDto testDto = new TestDto();
        testDto.setStr("hello world, this is the k8s provide service");
        log.info("this is the k9s provide service");
        return testDto;
    }
}
