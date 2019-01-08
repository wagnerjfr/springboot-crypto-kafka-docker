# springboot-crypto-kafka-docker

This project, in order to deploy Confluent Kafka in the machine, starts 4 Docker containers (ZooKeeper, Kafka, Kafka-REST-Proxy
and Kafka-Topics-UI). It also starts 3 other containers (1 Producer and 2 Consumers) to emulate message publish/consume
using crypto currencies JSON data. The Producer and Consumers images were developed using SpringBoot and the containers
are later started using Docker-Compose. The project uses Bitcoin and Litecoin JSON data from Bitstamp API.  

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
You can open a separate terminal and follow the logs while systems are initializing:
```
docker-compose logs -f
```
### 4. Check the results
After some seconds, the Producer starts publishing data and Consumers starts consuming it.

The Producer publishes data in 2 topics "BTC" and "LTC".

We have 2 Consumers (**consumer-btc** and **consumer-ltc**), each one reading just one of the topics. 

Docker-compose logs shows:
```console
producer            | JSON data for 'BTC' sent.
consumer-btc        | Topic - BTC, Partition - 0, Value: 3998.34
producer            | JSON data for 'LTC' sent.
consumer-ltc        | Topic - LTC, Partition - 0, Value: 37.62
producer            | JSON data for 'BTC' sent.
consumer-btc        | Topic - BTC, Partition - 0, Value: 3998.34
producer            | JSON data for 'LTC' sent.
consumer-ltc        | Topic - LTC, Partition - 0, Value: 37.62
```
Access <http://localhost:8085/> to see the topics and received data graphically.
![alt text](https://github.com/wagnerjfr/springboot-crypto-kafka-docker/blob/master/figures/kafka-topics-ui.png)

### 5. Clean up
Go to the root folder where is'docker-compose.yml'.

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
