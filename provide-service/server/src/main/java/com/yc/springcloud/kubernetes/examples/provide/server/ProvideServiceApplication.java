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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 服务提供方
 *
 * @author yangchuan
 * @version 1.0
 * @createDate 2020/1/5
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableSwagger2
@ComponentScan(basePackages = {"com.yc.springcloud.kubernetes.examples"})
public class ProvideServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProvideServiceApplication.class, args);
	}

}
