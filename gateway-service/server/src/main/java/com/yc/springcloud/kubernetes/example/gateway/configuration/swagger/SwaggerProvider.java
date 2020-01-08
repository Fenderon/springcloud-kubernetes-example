package com.yc.springcloud.kubernetes.example.gateway.configuration.swagger;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * SaggerProvider
 *
 * @author yangchuan
 * @version 1.0
 * @createDate 2020/1/8
 **/

@Component
@Primary
@AllArgsConstructor
public class SwaggerProvider implements SwaggerResourcesProvider {

    public static final String API_URI = "/v2/api-docs";

    private final DiscoveryClientRouteDefinitionLocator routeLocator;

    public static final String EUREKA_SUB_PRIX = "CompositeDiscoveryClient_";

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        routeLocator.getRouteDefinitions().subscribe(route ->
                resources.add(swaggerResource(route.getId().substring(EUREKA_SUB_PRIX.length()),
                        route.getPredicates().get(0).getArgs().get("pattern").replace("/**", API_URI)))
        );
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }

}
