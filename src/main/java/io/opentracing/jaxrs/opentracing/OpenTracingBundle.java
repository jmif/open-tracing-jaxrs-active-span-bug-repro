package io.opentracing.jaxrs.opentracing;

import brave.Tracing;
import brave.opentracing.BraveTracer;
import brave.sampler.Sampler;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.opentracing.Tracer;
import io.opentracing.contrib.jaxrs2.server.ServerTracingDynamicFeature;
import io.opentracing.contrib.jaxrs2.server.SpanFinishingFilter;
import io.opentracing.util.GlobalTracer;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.okhttp3.OkHttpSender;

import javax.inject.Provider;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class OpenTracingBundle<T extends Configuration> implements ConfiguredBundle<T> {

    private TracerProvider tracerProvider = new TracerProvider();

    @Override
    public void run(T configuration, Environment environment) throws Exception {
        Sender sender = OkHttpSender
                .newBuilder()
                .endpoint("http://zipkin-endpoint")
                .compressionEnabled(true)
                .build();

        AsyncReporter<Span> reporter = AsyncReporter.builder(sender).build();

        Tracer tracer = BraveTracer
                .newBuilder(
                        Tracing.newBuilder()
                                .localServiceName("service-name")
                                .spanReporter(reporter)
                                .sampler(Sampler.ALWAYS_SAMPLE)
                                .build()
                )
                .build();

        tracerProvider.setTracer(tracer);

        GlobalTracer.register(tracer);

        // Register feature / servlet filter to start traces
        environment.jersey().register(
                new ServerTracingDynamicFeature.Builder(tracer).build()
        );

        // Register filter to end traces
        FilterRegistration.Dynamic registration = environment.servlets()
                .addFilter("tracing-filter", new SpanFinishingFilter(tracer));

        registration.setAsyncSupported(true);
        registration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "*");

        environment.lifecycle().manage(new ZipkinManagedReporter(reporter));
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {}

    public TracerProvider getTracerProvider() {
        return tracerProvider;
    }

    public static class TracerProvider implements Provider<Tracer> {

        private Tracer tracer = null;

        void setTracer(Tracer tracer) {
            this.tracer = tracer;
        }

        @Override
        public Tracer get() {
            return this.tracer;
        }
    }
}
