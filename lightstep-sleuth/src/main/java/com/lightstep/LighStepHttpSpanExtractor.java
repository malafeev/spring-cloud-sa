package com.lightstep;

import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanTextMap;
import org.springframework.cloud.sleuth.instrument.web.HttpSpanExtractor;
import org.springframework.cloud.sleuth.util.TextMapUtil;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;


public class LighStepHttpSpanExtractor implements HttpSpanExtractor {
    @Override
    public Span joinTrace(SpanTextMap textMap) {
        Map<String, String> carrier = TextMapUtil.asMap(textMap);

        if (!carrier.containsKey("ot-tracer-spanid") || !carrier.containsKey("ot-tracer-traceid")) {
            return null;
        }

        long traceId = unHex(carrier.get("ot-tracer-traceid"));
        long spanId = unHex(carrier.get("ot-tracer-spanid"));

        Map<String, String> decodedBaggage = new HashMap<>();

        for (Map.Entry<String, String> entry : textMap) {
            if (entry.getKey().startsWith("ot-baggage-")) {
                decodedBaggage.put(entry.getKey().substring("ot-baggage-".length()), entry.getValue());
            }
        }

        Span.SpanBuilder builder = Span.builder().traceId(traceId).spanId(spanId).baggage(decodedBaggage);
        return builder.build();
    }

    private static long unHex(String hexString) throws NumberFormatException {
        return new BigInteger(hexString, 16).longValue();
    }
}
