package com.lightstep;

import com.lightstep.tracer.jre.JRETracer;
import io.opentracing.Tracer;
import java.net.MalformedURLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.SpanReporter;
import org.springframework.cloud.sleuth.instrument.web.HttpSpanExtractor;
import org.springframework.cloud.sleuth.instrument.web.HttpSpanInjector;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:lightstep.properties")
public class LightStepConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(LightStepConfiguration.class);

  @Value("${spring.application.name}")
  private String applicationName;

  @Value("${lightstep.access_token}")
  private String accessToken;

  @Value("${lightstep.component_name}")
  private String componentName;

  @Value("${lightstep.collector_host}")
  private String host;

  @Value("${lightstep.collector_port}")
  private int port;

  @Bean
  public Tracer lightStepTracer() {
    try {
      return new JRETracer(
          new com.lightstep.tracer.shared.Options.OptionsBuilder()
              .withAccessToken(accessToken)
              .withComponentName(componentName)
              .withCollectorHost(host)
              .withCollectorPort(port)
              .build()
      );
    } catch (MalformedURLException e) {
      logger.error("Failed to init tracer", e);
    }

    return null;
  }

  @Bean
  public Sampler defaultSampler() {
    return new AlwaysSampler();
  }

  @Bean
  public SpanReporter spanReporter() {
    return new LightStepSpanReporter(lightStepTracer(), applicationName);
  }

  @Bean
  public HttpSpanInjector lightStepHttpSpanInjector() {
    return new LightStepHttpSpanInjector();
  }

  @Bean
  public HttpSpanExtractor lightStepHttpSpanExtractor() {
    return new LighStepHttpSpanExtractor();
  }

}
