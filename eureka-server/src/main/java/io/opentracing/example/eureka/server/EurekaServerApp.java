package io.opentracing.example.eureka.server;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApp {

  public static void main(String[] args) {
    new SpringApplicationBuilder(EurekaServerApp.class).run(args);
  }

}
