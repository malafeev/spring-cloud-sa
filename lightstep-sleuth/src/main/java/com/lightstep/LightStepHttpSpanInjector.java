package com.lightstep;

import java.util.Map;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanTextMap;
import org.springframework.cloud.sleuth.instrument.web.HttpSpanInjector;


public class LightStepHttpSpanInjector implements HttpSpanInjector {

  @Override
  public void inject(Span span, SpanTextMap carrier) {

    carrier.put("ot-tracer-traceid", Long.toHexString(span.getTraceId()));
    carrier.put("ot-tracer-spanid", Long.toHexString(span.getSpanId()));
    carrier.put("ot-tracer-sampled", "true");

    for (Map.Entry<String, String> e : span.baggageItems()) {
      carrier.put("ot-baggage-" + e.getKey(), e.getValue());
    }

  }
}
