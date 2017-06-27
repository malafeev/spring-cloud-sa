package io.opentracing.contrib.springcloud;

import io.opentracing.Tracer;
import io.opentracing.rxjava.TracingRxJavaUtils;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingRxAutoConfiguration {

  @Autowired
  private Tracer tracer;

  @PostConstruct
  public void init() {
    TracingRxJavaUtils.enableTracing(tracer);
  }
}
