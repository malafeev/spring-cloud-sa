package io.opentracing.cloud;

import io.opentracing.Tracer;
import io.opentracing.mock.MockTracer;
import io.opentracing.mock.MockTracer.Propagator;
import io.opentracing.util.ThreadLocalActiveSpanSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TracingFeignClientAutoConfiguration.class, TracingRxAutoConfiguration.class})
public class OpentracingCloudConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(OpentracingCloudConfiguration.class);

  @Bean
  public Tracer tracer() {
    return new MockTracer(new ThreadLocalActiveSpanSource(), Propagator.PRINTER);
  }

  /*
  @Bean
  public Tracer lightStepTracer() {
    try {
      return new JRETracer(
          new OptionsBuilder()
              .withAccessToken("bla-bla-bla")
              .withComponentName("spring-cloud")
              .build()
      );
    } catch (MalformedURLException e) {
      logger.error("Failed to init tracer", e);
    }

    return null;
  }
  */


}
