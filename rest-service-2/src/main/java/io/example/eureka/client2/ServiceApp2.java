package io.example.eureka.client2;


import com.lightstep.LightStepConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@EnableFeignClients
@EnableEurekaClient
@RestController
@Import(LightStepConfiguration.class)
@SpringBootApplication
public class ServiceApp2 {

  private final StoreClient storeClient;

  @Autowired
  public ServiceApp2(StoreClient storeClient) {
    this.storeClient = storeClient;
  }

  @Bean
  @LoadBalanced
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Autowired
  private RestTemplate restTemplate;

  @GetMapping("/")
  public String index() {
    return storeClient.get();
  }

  @GetMapping("/home")
  public String home() {
    return restTemplate.getForObject("http://rest-service-1/home", String.class);
  }

  public static void main(String[] args) {
    new SpringApplicationBuilder(ServiceApp2.class).web(true).run(args);
  }
}
