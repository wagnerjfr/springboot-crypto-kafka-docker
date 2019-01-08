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
import java.util.Properties;

@SpringBootApplication
public class KafkaDockerProducerApplication implements CommandLineRunner {

	private KafkaProducer<String,String> kafkaProducer;

	public static void main(String[] args) {
		SpringApplication.run(KafkaDockerProducerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String bootstrap_server="localhost";
		Long initial_delay = 0L;

		if (args.length < 1) {
			System.err.println("java -jar target/kafka-docker-producer <bootstrap-server> [<initial_delay>]");
			System.err.println("Example: java -jar target/kafka-docker-producer localhost:29092 1000");
			System.exit(1);
		}
		else {
			bootstrap_server = args[0];
			initial_delay = (args.length == 2) ? Long.valueOf(args[1]) : 0;
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
				sendData("BTC", "https://www.bitstamp.net/api/v2/ticker_hour/btcusd/");
				sendData("LTC", "https://www.bitstamp.net/api/v2/ticker_hour/ltcusd/");
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
					System.out.println(String.format("JSON data for '%s' sent.", topic));
			}
		);
		Thread.sleep(3000);
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

