import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class javasend {
    public static void main(String[] args) {
        // Kafka broker configuration
        String kafkaBroker = "localhost:9092";
        String kafkaTopic = "tosend";

        // Producer configuration
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaBroker);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // Created a Kafka producer instance
        Producer<String, String> producer = new KafkaProducer<>(props);

        // Message payload
        String payload = "Hello, World!";

        // Create a ProducerRecord with the topic and payload
        ProducerRecord<String, String> record = new ProducerRecord<>(kafkaTopic, payload);

        // Send the message asynchronously
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception == null) {
                    System.out.println("Message sent successfully to partition " + metadata.partition()
                            + " at offset " + metadata.offset());
                } else {
                    System.err.println("Error sending message: " + exception.getMessage());
                }
            }
        });

        // Closed the producer
        producer.close();
    }
}

