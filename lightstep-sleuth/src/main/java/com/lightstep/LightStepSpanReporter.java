package com.lightstep;


import com.lightstep.tracer.shared.SpanBuilder;
import com.lightstep.tracer.shared.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;
import java.net.MalformedURLException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.Log;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanReporter;

public class LightStepSpanReporter implements SpanReporter {

  private static final Logger logger = LoggerFactory.getLogger(LightStepConfiguration.class);

  private final Tracer tracer;
  private final String applicationName;

  LightStepSpanReporter(Tracer tracer, String applicationName) {
    this.tracer = tracer;
    this.applicationName = applicationName;
  }

  /**
   * Convert {@link Span} to {@link com.lightstep.tracer.shared.Span}
   */
  private void convertAndReport(Span span) throws MalformedURLException {
    SpanBuilder lightStepSpanBuilder = (SpanBuilder) tracer.buildSpan(span.getName())
        .withStartTimestamp(span.getBegin() * 1000L);

    lightStepSpanBuilder.withTraceIdAndSpanId(span.getTraceId(), span.getSpanId());

    if (!span.getParents().isEmpty()) {
      lightStepSpanBuilder.asChildOf(new SpanContext(span.getTraceId(), span.getParents().get(0)));
    }
    com.lightstep.tracer.shared.Span lightStepSpan = (com.lightstep.tracer.shared.Span) lightStepSpanBuilder
        .start();

    for (Map.Entry<String, String> entry : span.baggageItems()) {
      lightStepSpan.setBaggageItem(entry.getKey(), entry.getValue());
    }

    for (Log log : span.logs()) {
      lightStepSpan.log(log.getTimestamp() * 1000L, log.getEvent());
      //System.out.println("LOG: " + log.getEvent());
    }

    for (Map.Entry<String, String> entry : span.tags().entrySet()) {
      lightStepSpan.setTag(entry.getKey(), entry.getValue());
      //System.out.println(entry.getKey() + ":" + entry.getValue());
    }

    lightStepSpan.setTag(Tags.COMPONENT.getKey(), applicationName);

    lightStepSpan.finish(span.getEnd() * 1000L);

    logger.info("Span: {} {}", span.getName(), span);
  }

  @Override
  public void report(Span span) {
    try {
      convertAndReport(span);
    } catch (MalformedURLException e) {
      logger.error("failed to report span", e);
    }
  }
}
