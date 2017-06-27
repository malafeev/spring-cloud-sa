package io.opentracing.example.client2;

import org.springframework.stereotype.Component;

@Component
public class StoreClientFallback implements StoreClient {

  @Override
  public String get() {
    return "Fallback";
  }
}