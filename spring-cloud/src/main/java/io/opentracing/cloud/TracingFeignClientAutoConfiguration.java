package io.opentracing.cloud;

import feign.Client;
import feign.Feign;
import feign.Retryer;
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
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class TracingFeignClientAutoConfiguration {

  @Autowired
  private Tracer tracer;

  @PostConstruct
  public void init() {
    TracingConcurrencyStrategy.register(tracer);
  }

  @Bean
  @ConditionalOnMissingBean
  @Scope("prototype")
  Feign.Builder feignBuilder(BeanFactory beanFactory) {
    return builder(beanFactory);
  }

  private Feign.Builder builder(BeanFactory beanFactory) {
    return Feign.builder().retryer(Retryer.NEVER_RETRY)
        .client(client(beanFactory));
  }

  private Client client(BeanFactory beanFactory) {
    Client client = beanFactory.getBean(Client.class);
    return new TracingClient(client, tracer);
  }
}
