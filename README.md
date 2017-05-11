# Spring Cloud Sleuth LightStep Instrumentation Example

Example consists of:
- Eureka Server
- Rest Service 1
- Rest Service 2

Rest services are registered in Eureka on startup.

Service 2 make rest request to Service 1 via Feign client which uses Service Discovery, Circuit Breaker (Hystrix) 
and Client Side Load Balancer (Ribbon)

## Configuration

Define next properties:
```properties
lightstep.access_token=access key
lightstep.component_name=spring-cloud
lightstep.collector_host=collector-grpc.lightstep.com
lightstep.collector_port=443
```

## Usage

Build 
```bash
mvn clean package
```

Start Eureka
```bash
java -jar eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar
```

Start Rest Service 1
```bash
java -jar rest-service-1/target/rest-service-1-0.0.1-SNAPSHOT.jar
```

Start Rest Service 2
```bash
java -jar rest-service-2/target/rest-service-2-0.0.1-SNAPSHOT.jar
```

Make HTTP Request to Service 2:
```bash
curl http://localhost:8082/
```