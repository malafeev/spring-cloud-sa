package io.example.eureka.client2;


import io.opentracing.Tracer;
import io.opentracing.cloud.OpentracingCloudConfiguration;
import io.opentracing.contrib.spring.web.client.TracingRestTemplateInterceptor;
import io.opentracing.mock.MockTracer;
import javax.annotation.PostConstruct;
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
@Import(OpentracingCloudConfiguration.class)
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
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate;
  }

  @PostConstruct
  public void addRestTemplateInterceptor() {
    restTemplate.getInterceptors().add(new TracingRestTemplateInterceptor(tracer));
  }

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private Tracer tracer;

  @GetMapping("/")
  public String index() {
    String s = storeClient.get();

    MockTracer mockTracer = (MockTracer) tracer;

    System.out.println(mockTracer.finishedSpans().size());
    return s;
  }

  @GetMapping("/headers")
  public String home() {
    return restTemplate.getForObject("http://rest-service-1/headers", String.class);
  }

  public static void main(String[] args) {
    new SpringApplicationBuilder(ServiceApp2.class).web(true).run(args);
  }
}
