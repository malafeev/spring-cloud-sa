package io.opentracing.contrib.springcloud;

import feign.Client;
import feign.Feign.Builder;
import feign.opentracing.TracingClient;
import io.opentracing.Tracer;


public class TracingBuilder extends Builder {

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
