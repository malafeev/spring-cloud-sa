package io.opentracing.contrib.springcloud;

import feign.Client;
import feign.Feign;
import feign.opentracing.TracingClient;
import feign.opentracing.hystrix.TracingConcurrencyStrategy;
import io.opentracing.Tracer;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.feign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AutoConfigureBefore({FeignAutoConfiguration.class})
public class TracingFeignClientAutoConfiguration {

  private final Tracer tracer;

  @Autowired
  public TracingFeignClientAutoConfiguration(Tracer tracer) {
    this.tracer = tracer;
  }

  @PostConstruct
  public void init() {
    // Enable Hystrix Feign tracing
    TracingConcurrencyStrategy.register(tracer);
  }

  @Bean
  @ConditionalOnMissingBean
  @Scope("prototype")
  public Feign.Builder feignBuilder(BeanFactory beanFactory) {
    return new TracingBuilder(tracer).client(client(beanFactory));
  }

  private Client client(BeanFactory beanFactory) {
    Client client = beanFactory.getBean(Client.class);
    return new TracingClient(client, tracer);
  }
}


