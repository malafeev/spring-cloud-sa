package com.lightstep;

import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanTextMap;
import org.springframework.cloud.sleuth.instrument.web.ZipkinHttpSpanExtractor;
import org.springframework.cloud.sleuth.util.TextMapUtil;


public class LightStepHttpSpanExtractor extends ZipkinHttpSpanExtractor {

  public LightStepHttpSpanExtractor(Pattern skipPattern) {
    super(skipPattern);
  }

  @Override
  public Span joinTrace(SpanTextMap textMap) {
    Map<String, String> carrier = TextMapUtil.asMap(textMap);

    String traceId = carrier.get("ot-tracer-traceid");
    String spanId = carrier.get("ot-tracer-spanid");

    if (traceId == null || spanId == null) {
      return null;
    }

    textMap.put(Span.TRACE_ID_NAME, traceId);
    textMap.put(Span.SPAN_ID_NAME, spanId);

    for (Map.Entry<String, String> entry : textMap) {
      if (entry.getKey().startsWith("ot-baggage-")) {
        textMap.put(entry.getKey().replace("ot-baggage", Span.SPAN_BAGGAGE_HEADER_PREFIX),
            entry.getValue());
      }
    }

    return super.joinTrace(textMap);
  }
}
