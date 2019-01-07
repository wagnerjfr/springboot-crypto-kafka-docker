FROM openjdk:8-jdk-alpine

LABEL maintainer="wagnerjfr@yahoo.com.br"

ENV TOPIC=""

RUN apk update && apk add ca-certificates wget && update-ca-certificates

ADD kafka-docker-consumer*.jar /kafka-docker-consumer.jar

ENTRYPOINT java -jar /kafka-docker-consumer.jar $TOPIC