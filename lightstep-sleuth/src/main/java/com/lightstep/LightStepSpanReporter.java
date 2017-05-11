package com.lightstep;


import com.lightstep.tracer.shared.SpanBuilder;
import com.lightstep.tracer.shared.SpanContext;
import io.opentracing.util.GlobalTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.Log;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanReporter;

import java.net.MalformedURLException;
import java.util.Map;


public class LightStepSpanReporter implements SpanReporter {
    private static final Logger logger = LoggerFactory.getLogger(LightStepConfiguration.class);


    private com.lightstep.tracer.shared.Span convert(Span span) throws MalformedURLException {
        SpanBuilder lighStepSpanBuilder = (SpanBuilder) GlobalTracer.get().buildSpan(span.getName()).withStartTimestamp(span.getBegin());
        lighStepSpanBuilder.withTraceIdAndSpanId(span.getTraceId(), span.getSpanId());

        if (!span.getParents().isEmpty()) {
            lighStepSpanBuilder.asChildOf(new SpanContext(span.getTraceId(), span.getParents().get(0)));
        }
        com.lightstep.tracer.shared.Span lightStepSpan = (com.lightstep.tracer.shared.Span) lighStepSpanBuilder.start();


        for (Map.Entry<String, String> entry : span.baggageItems()) {
            lightStepSpan.setBaggageItem(entry.getKey(), entry.getValue());
        }

        for (Log log : span.logs()) {
            lightStepSpan.log(log.getTimestamp(), log.getEvent());
        }

        for (Map.Entry<String, String> entry : span.tags().entrySet()) {
            lightStepSpan.setTag(entry.getKey(), entry.getValue());
        }

        lightStepSpan.finish(span.getEnd());

        logger.info("Span: {}", span.getName());

        return lightStepSpan;
    }

    @Override
    public void report(Span span) {

        try {
            convert(span);
        } catch (MalformedURLException e) {
            logger.error("fail", e);
        }
    }
}
