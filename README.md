# springboot-crypto-kafka-docker

In order to deploy Confluent Kafka in the machine, this project starts 4 Docker containers (ZooKeeper, Kafka, Kafka-REST-Proxy and Kafka-Topics-UI). It also starts 3 other containers (1 Producer and 2 Consumers) to emulate message publish/consume using crypto currencies JSON data. The Producer and Consumers images were developed using SpringBoot and the containers are later started using Docker-Compose. The project uses Bitcoin, Litecoin, and other currency JSON data from Bitstamp API.  

## Steps

### 1. Clone and build the projet
Run the command below to clone the project:
```
git clone https://github.com/wagnerjfr/springboot-crypto-kafka-docker.git
```
After the process is finished, access the project folder and execute the maven command:
```
cd springboot-crypto-kafka-docker
mvn clean package -DskipTests
```
You should expect something like the below output log in the terminal:
```console
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO] 
[INFO] kafka-docker ....................................... SUCCESS [  1.405 s]
[INFO] kafka-docker-producer .............................. SUCCESS [  2.219 s]
[INFO] kafka-docker-consumer .............................. SUCCESS [  0.526 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 4.904 s
[INFO] Finished at: 2019-01-07T21:12:52-02:00
[INFO] Final Memory: 38M/270M
[INFO] ------------------------------------------------------------------------
```
### 2. Build the Producer and Consumer Docker Images
Now we are going to build the Docker images for the modules *kafka-docker-producer* and *kafka-docker-consumer*:
#### kafka-docker-producer
```
cd kafka-docker-producer
mvn clean package docker:build -DskipTests
```
Expected output:
```console
Successfully built e11af0e5cbca
Successfully tagged docker.mycompany.com/kafka-docker-producer:latest
[INFO] Built docker.mycompany.com/kafka-docker-producer
[INFO] Tagging docker.mycompany.com/kafka-docker-producer with 0.0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 10.617 s
[INFO] Finished at: 2019-01-07T21:22:37-02:00
[INFO] Final Memory: 54M/412M
[INFO] ------------------------------------------------------------------------
```
#### kafka-docker-consumer
```
cd ../kafka-docker-consumer
mvn clean package docker:build -DskipTests
```
Expected output:
```console
Successfully built 0e9497736872
Successfully tagged docker.mycompany.com/kafka-docker-consumer:latest
[INFO] Built docker.mycompany.com/kafka-docker-consumer
[INFO] Tagging docker.mycompany.com/kafka-docker-consumer with 0.0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 10.813 s
[INFO] Finished at: 2019-01-07T21:24:41-02:00
[INFO] Final Memory: 54M/411M
[INFO] ------------------------------------------------------------------------
```
List the Docker images and check the new images by executing:
```
docker images
```
These are the new images created:
```console
REPOSITORY                                     TAG                 IMAGE ID            CREATED             SIZE
docker.mycompany.com/kafka-docker-consumer     0.0.1-SNAPSHOT      0e9497736872        36 minutes ago      133MB
docker.mycompany.com/kafka-docker-consumer     latest              0e9497736872        36 minutes ago      133MB
docker.mycompany.com/kafka-docker-producer     0.0.1-SNAPSHOT      e11af0e5cbca        39 minutes ago      133MB
docker.mycompany.com/kafka-docker-producer     latest              e11af0e5cbca        39 minutes ago      133MB
```
### 3. Create the containers
We are using Docker-Compose to start the containers. Go to the root folder where 'docker-compose.yml' is located and run the below command:
```
docker-compose up -d
```
[Optional] You can either open a separate terminal and follow the logs while systems are initializing:
```
docker-compose logs -f
```
[Optional] Or check the starting status:
```
docker-compose ps
```
### 4. Check the results
After some seconds, the Producer starts publishing data and Consumers start consuming it.

The Producer publishes data in 2 topics "BTC" and "LTC".

We have 2 Consumers (**consumer-btc** and **consumer-ltc**), each one reading just one of the topics. 

Docker-compose logs shows:
```console
producer            | [17:20:33.090] JSON data sent to topic LTC.
consumer-ltc        | [17:20:33.092] Topic - LTC, Partition - 0, Value: 39.39
consumer-btc        | [17:20:36.364] Topic - BTC, Partition - 0, Value: 4014.00
producer            | [17:20:36.363] JSON data sent to topic BTC.
producer            | [17:20:39.642] JSON data sent to topic LTC.
consumer-ltc        | [17:20:39.644] Topic - LTC, Partition - 0, Value: 39.39
producer            | [17:20:42.916] JSON data sent to topic BTC.
consumer-btc        | [17:20:42.919] Topic - BTC, Partition - 0, Value: 4014.00
producer            | [17:20:46.201] JSON data sent to topic LTC.
consumer-ltc        | [17:20:46.203] Topic - LTC, Partition - 0, Value: 39.39
producer            | [17:20:49.573] JSON data sent to topic BTC.
consumer-btc        | [17:20:49.574] Topic - BTC, Partition - 0, Value: 4014.00
```
Access <http://localhost:8085/> to see the topics and received data graphically.
![alt text](https://github.com/wagnerjfr/springboot-crypto-kafka-docker/blob/master/figures/kafka-topics-ui.png)

### 5. Running extra containers and/or jar files
It's possible to execute more jar files to produce and consume different currencies using the same Kafka.

Open a different terminal, go to project root folder and start a producer jar to publish BCH and ETH prices.
```
java -jar kafka-docker-producer/target/kafka-docker-producer-0.0.1-SNAPSHOT.jar localhost:29092 1000 3000 BCH,ETH
```
Parameters:
1. KAFKA_BOOTSTRAP_SERVER (Kafka server): **localhost:29092**
2. INITIAL_DELAY (amount of time in milliseconds to wait before starting producing): **1000** (1s)
3. INTERVAL (amount of time in milliseconds between messages): **3000** (3s)
4. TOPICS (Kafka topics): **BCH,ETH**

In another terminal, start a consumer jar to read BCH and ETH prices.

***IMPORTANT: Each crypto currency producer's topic (BTC, LTC, etc) has just one partition. Use different group ids
when creating a consumer to avoid creating a consumer which will be part of an existing consumer group. If it happens,
having just one partition, this new consumer instance will just receive data when the first consumer in the group stops.***

```
java -jar kafka-docker-consumer/target/kafka-docker-consumer-0.0.1-SNAPSHOT.jar localhost:29092 my-group BCH,ETH
```
Parameters:

1. KAFKA_BOOTSTRAP_SERVER (Kafka server): **localhost:29092**
2. GROUP_ID (consumer's group): **my-group**
3. TOPICS (Kafka topics): **BCH,ETH**

***To stop the executions type Ctrl+C.***

Let's now start containers to produce and consume XRP prices.

In a different terminal, execute the command below to create the producer:
```
docker run -t --net springboot-crypto-kafka-docker_default --name producer-xrp \
   -e KAFKA_BOOTSTRAP_SERVER='kafka:9092' \
   -e INITIAL_DELAY=1000 \
   -e INTERVAL=3000 \
   -e TOPICS='XRP'  \
   docker.mycompany.com/kafka-docker-producer:latest
```
In another terminal, execute the command below to create the consumer:
```
docker run -t --net springboot-crypto-kafka-docker_default --name consumer-xrp \
   -e KAFKA_BOOTSTRAP_SERVER='kafka:9092' \
   -e KAFKA_GROUP_ID='my-group' \
   -e TOPICS='XRP' \
   docker.mycompany.com/kafka-docker-consumer:latest
```
To stop the containers:
```
docker stop producer-xrp consumer-xrp
```
To remove the stopped containers:
```
docker rm producer-xrp consumer-xrp
```

### 6. Clean up
Go to the root folder where is *docker-compose.yml*.

To stop all containers execute:
```
docker-compose down
```
In your terminal, you should see:
```console
Stopping producer         ... done
Stopping consumer-ltc     ... done
Stopping consumer-btc     ... done
Stopping kafka-topics-ui  ... done
Stopping kafka-rest-proxy ... done
Stopping kafka            ... done
Stopping zookeeper        ... done
Removing producer         ... done
Removing consumer-ltc     ... done
Removing consumer-btc     ... done
Removing kafka-topics-ui  ... done
Removing kafka-rest-proxy ... done
Removing kafka            ... done
Removing zookeeper        ... done
Removing network springboot-crypto-kafka-docker_default
```
Removing the Docker images created:
```
docker rmi docker.mycompany.com/kafka-docker-consumer:0.0.1-SNAPSHOT
docker rmi docker.mycompany.com/kafka-docker-consumer:latest
docker rmi docker.mycompany.com/kafka-docker-producer:0.0.1-SNAPSHOT
docker rmi docker.mycompany.com/kafka-docker-producer:latest
```
