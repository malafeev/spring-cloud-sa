# Spring Cloud OpenTracing  Instrumentation Example

OpenTracing  instrumentation for Spring Cloud

Example consists of:
- Eureka Server
- Rest Service 1
- Rest Service 2

Rest services are registered in Eureka on startup.

_Service 2_ make rest request to _Service 1_ via Feign client which uses Service Discovery, Circuit Breaker (Hystrix) 
and Client Side Load Balancer (Ribbon)


## Configuration

`ServiceApp.java` and `ServiceApp2.java` contains LightStep Tracer bean definitions.
It should be replaced with another `Tracer` implementation or should be provided access token.


## Usage

Build 
```bash
mvn clean package
```

Start Eureka
```bash
java -jar eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar
```

Access Eureka: `http://localhost:8761/`


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

## Problem with LightStep tracer

Because LightStep tracer depends on `grpc-netty`: 

```java
java.lang.IllegalArgumentException: Jetty ALPN/NPN has not been properly configured.
at io.grpc.netty.GrpcSslContexts.selectApplicationProtocolConfig(GrpcSslContexts.java:174) ~[grpc-netty-1.4.0.jar!/:1.4.0]
at io.grpc.netty.GrpcSslContexts.configure(GrpcSslContexts.java:151) ~[grpc-netty-1.4.0.jar!/:1.4.0]
at io.grpc.netty.GrpcSslContexts.configure(GrpcSslContexts.java:139) ~[grpc-netty-1.4.0.jar!/:1.4.0]
at io.grpc.netty.GrpcSslContexts.forClient(GrpcSslContexts.java:109) ~[grpc-netty-1.4.0.jar!/:1.4.0]
```
