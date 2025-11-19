FROM amazoncorretto:21-al2-generic

ARG JAR_FILE=build/libs/*.jar

RUN mkdir /build
WORKDIR /build

COPY ${JAR_FILE} /build/

EXPOSE 8080

# ENTRYPOINT ["java","-jar","/build/agg-kfk-consumer-ws.jar"]