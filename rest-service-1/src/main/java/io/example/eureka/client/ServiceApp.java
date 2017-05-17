package io.example.eureka.client;


import com.lightstep.LightStepConfiguration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
    return "Hello from Service 1";
  }

  @RequestMapping("/headers")
  public ResponseEntity<?> home(@RequestHeader HttpHeaders headers) {
    Map<String, String> map = new HashMap<>();
    headers.toSingleValueMap().forEach(map::put);
    return ResponseEntity.ok(map);
  }

  public static void main(String[] args) {
    new SpringApplicationBuilder(ServiceApp.class).web(true).run(args);
  }
}
