package com.lightstep;

import com.lightstep.tracer.jre.JRETracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.SpanReporter;
import org.springframework.cloud.sleuth.instrument.web.HttpSpanExtractor;
import org.springframework.cloud.sleuth.instrument.web.HttpSpanInjector;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
@PropertySource("classpath:lightstep.properties")
public class LightStepConfiguration {

    @Value("${lightstep.access_token}")
    private String accessToken;

    @Value("${lightstep.component_name}")
    private String componentName;

    @Value("${lightstep.collector_host}")
    private String host;

    @Value("${lightstep.collector_port}")
    private int port;

    @PostConstruct
    public void lightStepTracer() throws IOException {
        JRETracer tracer = new JRETracer(
                new com.lightstep.tracer.shared.Options.OptionsBuilder()
                        .withAccessToken(accessToken)
                        .withComponentName(componentName)
                        .withCollectorHost(host)
                        .withCollectorPort(port)
                        .withDisableReportingLoop(true) //TODO: remove
                        .build()
        );
        GlobalTracer.register(tracer);
    }


    @Bean
    public Sampler defaultSampler() {
        return new AlwaysSampler();
    }

    @Bean
    public SpanReporter spanReporter() {
        return new LightStepSpanReporter();
    }

    @Bean
    public HttpSpanInjector lightStepHttpSpanInjector() {
        return new LightStepHttpSpanInjector();
    }

    @Bean
    public HttpSpanExtractor lightStepHttpSpanExtractor() {
        return new LighStepHttpSpanExtractor();
    }

}
