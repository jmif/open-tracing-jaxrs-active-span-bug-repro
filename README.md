# OpenTracingActiveSpanBugRepro

How to start the OpenTracingActiveSpanBugRepro application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/active-span-bug-repro-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Running with maven
------------------

mvn exec:java -Dexec.mainClass="io.opentracing.jaxrs.OpenTracingActiveSpanBugReproApplication" -Dexec.args="server config.yml"
