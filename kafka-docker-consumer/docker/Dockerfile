FROM openjdk:8-jdk-alpine

LABEL maintainer="wagnerjfr@yahoo.com.br"

ENV KAFKA_BOOTSTRAP_SERVER ""
ENV KAFKA_GROUP_ID ""
ENV TOPIC ""

RUN apk update && apk add ca-certificates wget && update-ca-certificates

ADD kafka-docker-consumer*.jar /kafka-docker-consumer.jar

ENTRYPOINT java -jar /kafka-docker-consumer.jar $KAFKA_BOOTSTRAP_SERVER $KAFKA_GROUP_ID $TOPIC