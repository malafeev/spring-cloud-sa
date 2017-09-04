package io.opentracing.example.eureka.server;

import com.lightstep.tracer.jre.JRETracer;
import com.lightstep.tracer.shared.Options.OptionsBuilder;
import io.opentracing.NoopTracerFactory;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApp {

  private static final Logger logger = LoggerFactory.getLogger(EurekaServerApp.class);

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

  public static void main(String[] args) {
    new SpringApplicationBuilder(EurekaServerApp.class).web(true).run(args);
  }

}
