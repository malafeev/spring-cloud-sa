package io.opentracing.example.client2;


import com.lightstep.tracer.jre.JRETracer;
import com.lightstep.tracer.shared.Options.OptionsBuilder;
import io.opentracing.NoopTracerFactory;
import io.opentracing.Tracer;
import io.opentracing.contrib.spring.web.client.TracingRestTemplateInterceptor;
import java.net.MalformedURLException;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@EnableFeignClients
@EnableEurekaClient
@RestController
@SpringBootApplication
public class ServiceApp2 {

  private static final Logger logger = LoggerFactory.getLogger(ServiceApp2.class);

  private final StoreClient storeClient;

  @Autowired
  public ServiceApp2(StoreClient storeClient) {
    this.storeClient = storeClient;
  }

  @Bean
  public Tracer lightStepTracer() {
    try {
      return new JRETracer(
          new OptionsBuilder()
              .withAccessToken("bla-bla-bla")
              .withComponentName("spring-cloud")
              .build()
      );
    } catch (Exception e) {
      logger.error("Failed to init tracer", e);
    }
    return NoopTracerFactory.create();
  }

  @Bean
  @LoadBalanced
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate;
  }

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private Tracer tracer;

  @PostConstruct
  public void addRestTemplateInterceptor() {
    // We need manually add tracing interceptor because of @LoadBalanced RestTemplate
    restTemplate.getInterceptors().add(new TracingRestTemplateInterceptor(tracer));
  }

  @GetMapping("/")
  public String index() {
    return storeClient.get();
  }

  @GetMapping("/headers")
  public String home() {
    return restTemplate.getForObject("http://rest-service-1/headers", String.class);
  }

  public static void main(String[] args) {
    new SpringApplicationBuilder(ServiceApp2.class).web(true).run(args);
  }
}
