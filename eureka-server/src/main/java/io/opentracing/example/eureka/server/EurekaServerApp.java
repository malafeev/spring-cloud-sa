package io.opentracing.example.eureka.server;

import io.opentracing.Tracer;
import io.opentracing.mock.MockTracer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApp {

  //private static final Logger logger = LoggerFactory.getLogger(EurekaServerApp.class);

  @Bean
  public Tracer lightStepTracer() {
//    try {
//      return new JRETracer(
//          new OptionsBuilder()
//              .withAccessToken("bla-bla-bla")
//              .withComponentName("spring-cloud")
//              .build()
//      );
//    } catch (Exception e) {
//      logger.error("Failed to init tracer", e);
//    }
    return new MockTracer();
  }

  public static void main(String[] args) {
    new SpringApplicationBuilder(EurekaServerApp.class).web(true).run(args);
  }

}
