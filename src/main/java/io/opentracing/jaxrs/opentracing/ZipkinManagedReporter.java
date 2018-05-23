package io.opentracing.jaxrs.opentracing;

import io.dropwizard.lifecycle.Managed;
import zipkin2.reporter.AsyncReporter;

public class ZipkinManagedReporter implements Managed  {

    private final AsyncReporter reporter;

    public ZipkinManagedReporter(AsyncReporter reporter) {
        this.reporter = reporter;
    }

    @Override
    public void start() throws Exception {
    }

    @Override
    public void stop() throws Exception {
        this.reporter.flush();
        this.reporter.close();
    }
}
