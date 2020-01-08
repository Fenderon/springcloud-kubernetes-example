package com.yc.springcloud.kubernetes.example.gateway.configuration.local;

import com.yc.springcloud.kubernetes.examples.httpclient.LocalServerListLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 自定义服务发现
 *
 * @author yangchuan
 * @version 1.0
 * @createDate 2020/1/7
 */
@Slf4j
public class CustomizeDiscoveryClient implements DiscoveryClient {

    private List<ServiceInstance> serviceInstances;

    private List<String> servers;

    private final LocalServerListLoader localServerListLoader;

    public CustomizeDiscoveryClient(LocalServerListLoader localServerListLoader) {

        this.localServerListLoader = localServerListLoader;

    }

    private void updateServerList() {
        final List<String> ss = new LinkedList<>();
        serviceInstances = localServerListLoader.getAllServer().stream().map(e -> {
                    ServiceInstance si = new ServiceInstance() {
                        @Override
                        public String getServiceId() {
                            return e.getMetaInfo().getAppName();
                        }

                        @Override
                        public String getHost() {
                            return e.getHost();
                        }

                        @Override
                        public int getPort() {
                            return e.getPort();
                        }

                        @Override
                        public boolean isSecure() {
                            return true;
                        }

                        @Override
                        public URI getUri() {
                            String scheme = (this.isSecure()) ? "https" : "http";
                            String uri = String.format("%s://%s:%s", scheme, this.getHost(),
                                    this.getPort());
                            return URI.create(uri);
                        }

                        @Override
                        public Map<String, String> getMetadata() {
                            return new HashMap<>();
                        }
                    };
                    ss.add(si.getServiceId());

                    return si;
                }
        ).collect(Collectors.toList());

        servers = ss;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        updateServerList();
        return serviceInstances.stream().filter(e -> {
            return e.getServiceId().equalsIgnoreCase(serviceId);
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> getServices() {
        updateServerList();
        return servers;
    }
}
