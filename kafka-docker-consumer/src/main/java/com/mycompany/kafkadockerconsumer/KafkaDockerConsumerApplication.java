package com.mycompany.kafkadockerconsumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.Arrays;

@Slf4j
@SpringBootApplication
public class KafkaDockerConsumerApplication implements CommandLineRunner {

    private boolean stopped = false;

    public static void main(String[] args) {
        SpringApplication.run(KafkaDockerConsumerApplication.class, args);
    }

    @Override
    public void run(String... args) {

        String bootstrapServer;
        List<String> topics;
        String groupId;

        if (args.length < 3) {
            log.error("java -jar target/kafka-docker-consumer <bootstrap_server> <group_id> <topics>");
            log.error("Example: java -jar target/kafka-docker-consumer localhost:29092 my-group BTC,LTC");
            System.exit(1);
        }

        bootstrapServer = args[0];
        groupId = args[1];
        topics = Arrays.asList(args[2].split(","));

        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServer);
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", groupId);

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(topics);
        try {
            while (!stopped) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord record : records) {
                    log.info(String.format("[%s] Topic - %s, Partition - %d, Value: %s", LocalDateTime.now().toString(), record.topic(),
                        record.partition(), getRecordCryptoCurrentValue(record)));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            kafkaConsumer.close();
        }
    }

    private String getRecordCryptoCurrentValue(ConsumerRecord record) {
        String info = record.value().toString();
        String value = info.substring(info.indexOf("\"last\""), info.indexOf(", \"times")).replace("\"", "").trim();
        return value.substring(value.indexOf(':') + 1).trim();
    }

    public void stop() {
        this.stopped = true;
    }
}