# springboot-crypto-kafka-docker

In order to deploy [Confluent Kafka](https://www.confluent.io/) in the machine, this project starts four Docker containers ([ZooKeeper](https://zookeeper.apache.org/), [Kafka](https://kafka.apache.org/), [Kafka-REST-Proxy](https://docs.confluent.io/3.0.0/kafka-rest/docs/index.html) and [Kafka-Topics-UI](http://kafka-topics-ui.landoop.com/)). 

It also starts three other containers (one Producer and two Consumers) to emulate message publish/consume using crypto currencies JSON data. The Producer and Consumers images were developed using [SpringBoot](https://spring.io/projects/spring-boot) and the containers are later started using [Docker-Compose](https://docs.docker.com/compose/). The project uses the [Bitstamp REST API](https://www.bitstamp.net/api/) to grab JSON data for Bitcoin, Litecoin, and other crypto currencies.  


## Full article
### [SpringBoot-Kafka Integration: Dynamic Crypto Price Tracking in Real Time](https://medium.com/gitconnected/springboot-kafka-integration-dynamic-crypto-price-tracking-in-real-time-948ed8990744)
_Learn how to leverage Kafka’s distributed streaming platform to get up-to-date crypto prices for your application_
