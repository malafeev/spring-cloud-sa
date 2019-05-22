.PHONY: build clean run

specialagent_jar := /Users/malafes/Downloads/opentracing-specialagent-1.1.3-SNAPSHOT.jar

build:
	mvn clean package -DskipTests

clean:
	mvn clean

run:
	java \
        -Dls.componentName=spring-cloud-config \
        -Dls.collectorHost=localhost \
        -Dls.collectorProtocol=http \
        -Dls.collectorPort=8360 \
        -Dls.accessToken=${LS_LOCAL_TOKEN} \
        -Dsa.tracer=lightstep \
        -javaagent:$(specialagent_jar) -jar target/config-0.0.1-SNAPSHOT.jar

run-no-agent:
	java -jar target/config-0.0.1-SNAPSHOT.jar
