package io.example.eureka.client;


import com.lightstep.LightStepConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableEurekaClient
@RestController
@Import(LightStepConfiguration.class)
@SpringBootApplication
public class ServiceApp {

  @RequestMapping("/")
  public String index(@RequestHeader HttpHeaders headers) {
    return "Hello from Service 1 Index";
  }

  @RequestMapping("/home")
  public String home(@RequestHeader HttpHeaders headers) {
    return "Hello from Service 1 Home";
  }

  public static void main(String[] args) {
    new SpringApplicationBuilder(ServiceApp.class).web(true).run(args);
  }
}
