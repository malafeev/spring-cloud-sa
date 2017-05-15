package com.lightstep;


import com.lightstep.tracer.shared.SpanBuilder;
import io.opentracing.Tracer;
import java.net.MalformedURLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanReporter;

public class LightStepSpanReporter implements SpanReporter {

  private static final Logger logger = LoggerFactory.getLogger(LightStepConfiguration.class);

  private final Tracer tracer;

  LightStepSpanReporter(Tracer tracer) {
    this.tracer = tracer;
  }

  /**
   * Convert {@link Span} to {@link com.lightstep.tracer.shared.Span}
   */
  private void convertAndReport(Span span) throws MalformedURLException {
    SpanBuilder lightStepSpanBuilder = (SpanBuilder) tracer.buildSpan(span.getName())
        .withStartTimestamp(span.getBegin() * 1000L);

    lightStepSpanBuilder.withTraceIdAndSpanId(span.getTraceId(), span.getSpanId());

        /*if (!span.getParents().isEmpty()) {
            lighStepSpanBuilder.asChildOf(new SpanContext(span.getTraceId(), span.getParents().get(0)));
        }*/
    com.lightstep.tracer.shared.Span lightStepSpan = (com.lightstep.tracer.shared.Span) lightStepSpanBuilder
        .start();

        /*for (Map.Entry<String, String> entry : span.baggageItems()) {
            lightStepSpan.setBaggageItem(entry.getKey(), entry.getValue());
        }

        for (Log log : span.logs()) {
            lightStepSpan.log(log.getTimestamp(), log.getEvent());
        }

        for (Map.Entry<String, String> entry : span.tags().entrySet()) {
            lightStepSpan.setTag(entry.getKey(), entry.getValue());
        }
        */

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
