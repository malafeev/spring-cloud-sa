.PHONY: build clean run

specialagent_jar := /Users/malafes/Downloads/opentracing-specialagent-1.2.0-SNAPSHOT.jar

build:
	mvn clean package -DskipTests

clean:
	mvn clean

run:
	java \
        -Dls.componentName=spring-cloud-zuul \
        -Dls.collectorHost=localhost \
        -Dls.collectorProtocol=http \
        -Dls.collectorPort=8360 \
        -Dls.accessToken=${LS_LOCAL_TOKEN} \
        -Dsa.tracer=lightstep \
        -javaagent:$(specialagent_jar) -jar target/zuul-0.0.1-SNAPSHOT.jar

run-no-agent:
	java -jar target/zuul-0.0.1-SNAPSHOT.jar


# curl http://localhost:8083