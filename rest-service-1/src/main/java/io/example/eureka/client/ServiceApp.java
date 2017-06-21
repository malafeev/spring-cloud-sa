package io.example.eureka.client;


import io.opentracing.ActiveSpan;
import io.opentracing.Tracer;
import io.opentracing.cloud.OpentracingCloudConfiguration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@EnableEurekaClient
@RestController
@Import(OpentracingCloudConfiguration.class)
@SpringBootApplication
public class ServiceApp {

  @Autowired
  private Tracer tracer;

  @GetMapping("/")
  public String index(@RequestHeader HttpHeaders headers) {

    /*Observable observable = Observable.range(1, 8)
        //.subscribeOn(Schedulers.io())
        //.observeOn(Schedulers.io())
        .map(integer -> integer * 2)
        .filter(integer -> integer % 2 == 0);

    observable.subscribe(o -> {
      System.out.println(o + " " + Thread.currentThread().getName());
    });*/

    ActiveSpan activeSpan = tracer.activeSpan();

    return "Hello from Service 1";
  }

  @GetMapping("/headers")
  public ResponseEntity<?> home(@RequestHeader HttpHeaders headers) {
    Map<String, String> map = new HashMap<>();
    headers.toSingleValueMap().forEach(map::put);
    return ResponseEntity.ok(map);
  }

  public static void main(String[] args) {
    new SpringApplicationBuilder(ServiceApp.class).web(true).run(args);
  }
}
