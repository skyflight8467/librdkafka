import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerExample {

    private static final String KAFKA_BROKER = "localhost:9092";
    private static final String KAFKA_TOPIC = "tosend";
    private static final String KAFKA_GROUP_ID = "my-consumer-group";

    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKER);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, KAFKA_GROUP_ID);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // Subscribe to the Kafka topic
        consumer.subscribe(Collections.singleton(KAFKA_TOPIC));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    String message = record.value();
                    System.out.println("Received Message: " + message);

                    // Check if the message matches the expected payload
                    if (message.equals("Hello, World!")) {
                        System.out.println("Message was sent successfully to the Kafka topic 'tosend'");
                    } else {
                        System.out.println("Message was NOT sent successfully to the Kafka topic 'tosend'");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
