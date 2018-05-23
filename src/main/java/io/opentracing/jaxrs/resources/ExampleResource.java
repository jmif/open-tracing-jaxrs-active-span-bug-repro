package io.opentracing.jaxrs.resources;

import io.opentracing.Scope;
import io.opentracing.Tracer;
import org.eclipse.microprofile.opentracing.Traced;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Traced
@Path("/")
public class ExampleResource {

    @GET
    public Response exampleMethod(@Context Tracer tracer) {
        return Response.ok().build();
    }
}
