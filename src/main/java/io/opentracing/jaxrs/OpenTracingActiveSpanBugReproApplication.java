package io.opentracing.jaxrs;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.opentracing.jaxrs.opentracing.OpenTracingBundle;
import io.opentracing.jaxrs.resources.ExampleResource;

public class OpenTracingActiveSpanBugReproApplication extends Application<OpenTracingActiveSpanBugReproConfiguration> {

    public static void main(final String[] args) throws Exception {
        new OpenTracingActiveSpanBugReproApplication().run(args);
    }

    @Override
    public String getName() {
        return "OpenTracingActiveSpanBugREpro";
    }

    @Override
    public void initialize(final Bootstrap<OpenTracingActiveSpanBugReproConfiguration> bootstrap) {
        bootstrap.addBundle(new OpenTracingBundle<>());
    }

    @Override
    public void run(final OpenTracingActiveSpanBugReproConfiguration configuration,
                    final Environment environment) {

        environment.jersey().register(ExampleResource.class);
    }

}
