package io.example.eureka.client2;


import com.lightstep.LightStepConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableFeignClients
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableEurekaClient
@RestController
@Import(LightStepConfiguration.class)
public class ServiceApp2 {

  private final StoreClient storeClient;

  @Autowired
  public ServiceApp2(StoreClient storeClient) {
    this.storeClient = storeClient;
  }

  @RequestMapping("/")
  public String home() {
    return storeClient.get();
  }

  public static void main(String[] args) {
    new SpringApplicationBuilder(ServiceApp2.class).web(true).run(args);
  }


}
