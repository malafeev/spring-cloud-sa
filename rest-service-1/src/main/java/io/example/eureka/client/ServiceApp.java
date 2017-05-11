package io.example.eureka.client;


import com.lightstep.LightStepConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableEurekaClient
@RestController
@Import(LightStepConfiguration.class)
public class ServiceApp {

    @RequestMapping("/")
    public String home(@RequestHeader HttpHeaders headers) {
        return "Hello from Service 1";
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(ServiceApp.class).web(true).run(args);
    }
}
