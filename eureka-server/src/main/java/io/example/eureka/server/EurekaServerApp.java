package io.example.eureka.server;

import io.opentracing.cloud.OpentracingCloudConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableEurekaServer
@Import(OpentracingCloudConfiguration.class)
public class EurekaServerApp {

  public static void main(String[] args) {
    new SpringApplicationBuilder(EurekaServerApp.class).web(true).run(args);
  }

}
