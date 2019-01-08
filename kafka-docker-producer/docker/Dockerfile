FROM openjdk:8-jdk-alpine

LABEL maintainer="wagnerjfr@yahoo.com.br"

ENV KAFKA_BOOTSTRAP_SERVER ""
ENV INITIAL_DELAY 0

RUN apk update && apk add ca-certificates wget && update-ca-certificates

ADD kafka-docker-producer*.jar /kafka-docker-producer.jar

ENTRYPOINT java -jar /kafka-docker-producer.jar $KAFKA_BOOTSTRAP_SERVER $INITIAL_DELAY