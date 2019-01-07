package com.mycompany.kafkadockerconsumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@SpringBootApplication
public class KafkaDockerConsumerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(KafkaDockerConsumerApplication.class, args);
	}

	@Override
	public void run(String... args) {
		String topic = args[0];

		Properties properties = new Properties();
		properties.put("bootstrap.servers", "kafka:9092");
		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("group.id", "my-group");

		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
		List<String> topics = new ArrayList<>();
		topics.add(topic);
		kafkaConsumer.subscribe(topics);
		try{
			while (true){
				ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
				for (ConsumerRecord record: records){
					System.out.println(String.format("Topic - %s, Partition - %d, Value: %s", record.topic(),
						record.partition(), getRecordCryptoCurrentValue(record)));
				}
			}
		}catch (Exception e){
			System.out.println(e.getMessage());
		}finally {
			kafkaConsumer.close();
		}
	}

	private String getRecordCryptoCurrentValue(ConsumerRecord record) {
		String info = (String) record.value();
		String value = info.substring(info.indexOf("\"last\""), info.indexOf(", \"times")).replace("\"", "").trim();
		return value.substring(value.indexOf(":") + 1).trim();
	}
}

