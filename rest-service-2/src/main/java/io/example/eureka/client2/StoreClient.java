package io.example.eureka.client2;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "rest-service-1", fallback = StoreClientFallback.class)
public interface StoreClient {

    @RequestMapping(method = RequestMethod.GET, value = "/")
    String get();

}


