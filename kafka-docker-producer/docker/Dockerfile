FROM openjdk:8-jdk-alpine

LABEL maintainer="wagnerjfr@yahoo.com.br"

RUN apk update && apk add ca-certificates wget && update-ca-certificates

ADD kafka-docker-producer*.jar /kafka-docker-producer.jar

CMD ["java", "-jar", "/kafka-docker-producer.jar"]