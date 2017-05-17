package com.lightstep;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanTextMap;
import org.springframework.cloud.sleuth.instrument.web.HttpSpanExtractor;
import org.springframework.cloud.sleuth.util.TextMapUtil;
import org.springframework.util.StringUtils;


public class LightStepHttpSpanExtractor implements HttpSpanExtractor {

  @Override
  public Span joinTrace(SpanTextMap textMap) {
    Map<String, String> carrier = TextMapUtil.asMap(textMap);

    if (!carrier.containsKey("ot-tracer-spanid") || !carrier.containsKey("ot-tracer-traceid")) {
      return null;
    }

    long traceId = unHex(carrier.get("ot-tracer-traceid"));
    long spanId = unHex(carrier.get("ot-tracer-spanid"));
    String name = carrier.get(Span.SPAN_NAME_NAME);

    Map<String, String> decodedBaggage = new HashMap<>();

    for (Map.Entry<String, String> entry : textMap) {
      if (entry.getKey().startsWith("ot-baggage-")) {
        decodedBaggage.put(entry.getKey().substring("ot-baggage-".length()), entry.getValue());
      }
    }

    Span.SpanBuilder builder = Span.builder().name(name).traceId(traceId).spanId(spanId)
        .baggage(decodedBaggage);

    if (carrier.containsKey(Span.PARENT_ID_NAME)) {
      builder.parent(Span.hexToId(carrier.get(Span.PARENT_ID_NAME)));
    }

    String processId = carrier.get(Span.PROCESS_ID_NAME);
    if (StringUtils.hasText(processId)) {
      builder.processId(processId);
    }

    return builder.build();
  }

  private static long unHex(String hexString) throws NumberFormatException {
    return new BigInteger(hexString, 16).longValue();
  }
}
