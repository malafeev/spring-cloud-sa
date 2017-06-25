package io.opentracing.cloud;

import feign.Client;
import feign.Feign;
import feign.Feign.Builder;
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
import org.springframework.cloud.netflix.feign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@AutoConfigureBefore({FeignAutoConfiguration.class, FeignRibbonClientAutoConfiguration.class})
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
    return new TracingBuilder(tracer).retryer(Retryer.NEVER_RETRY)
        .client(client(beanFactory));
  }

  private Client client(BeanFactory beanFactory) {
    Client client = beanFactory.getBean(Client.class);
    return new TracingClient(client, tracer);
  }
}


class TracingBuilder extends Builder {

  private final Tracer tracer;

  TracingBuilder(Tracer tracer) {
    this.tracer = tracer;
  }

  @Override
  public Builder client(Client client) {
    super.client(new TracingClient(client, tracer));
    return this;
  }
}
