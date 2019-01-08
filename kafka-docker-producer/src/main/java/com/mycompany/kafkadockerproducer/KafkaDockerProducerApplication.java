package com.mycompany.kafkadockerproducer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@SpringBootApplication
public class KafkaDockerProducerApplication implements CommandLineRunner {

	private KafkaProducer<String,String> kafkaProducer;
	private Long interval;

	public static void main(String[] args) {
		SpringApplication.run(KafkaDockerProducerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String bootstrap_server;
		Long initial_delay;
		List<CryptoType> listTopics;

		if (args.length < 4) {
			System.err.println("java -jar target/kafka-docker-producer <bootstrap-server> <initial_delay> <interval> <topic-1> -- <topic-n>");
			System.err.println("Example: java -jar target/kafka-docker-producer localhost:29092 1000 3000 BTC,LTC,ETH");
			System.exit(1);
		}

		listTopics = new ArrayList<>();
		bootstrap_server = args[0];
		initial_delay = Long.valueOf(args[1]);
		interval = Long.valueOf(args[2]);
		String[] topics = args[3].split(",");
		for (String t: topics) {
			listTopics.add(FactortyCryptoType.getCryptoType(t));
		}

		Thread.sleep(initial_delay);

		Properties properties = new Properties();
		properties.put("bootstrap.servers", bootstrap_server);
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("acks", "1");

		kafkaProducer = new KafkaProducer<>(properties);

		String data;
		try {
			while(true){
				for (CryptoType crypto: listTopics) {
					if (crypto != CryptoType.UNKNOW) {
						sendData(crypto.getInitials(), crypto.getUrl());
					}
				}
			}
		}
		finally {
			kafkaProducer.close();
		}
	}

	private void sendData(String topic, String url) throws InterruptedException, IOException {
		String data = getRequest(url);
		ProducerRecord<String, String> record = new ProducerRecord<>(topic, topic, data);
		kafkaProducer.send(record, (recordMetadata, e) -> {
				if (e != null)
					e.printStackTrace();
				else
					System.out.println(String.format("[%s] JSON data sent to topic %s.", LocalTime.now().toString(), topic));
			}
		);
		Thread.sleep(interval);
	}

	private String getRequest(String url) throws IOException {
		StringBuilder response = new StringBuilder();

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}

		in.close();

		return response.toString();
	}
}

