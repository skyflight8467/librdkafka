#include <stdio.h>
#include <string.h>
#include <rdkafka.h>

// Kafka broker configuration
#define KAFKA_BROKER "localhost:9092"
#define KAFKA_TOPIC "tosend"

int main() {
    rd_kafka_t *rk;         // Kafka producer instance
    rd_kafka_conf_t *conf;  // Kafka configuration
    char errstr[512];       // Error buffer

    // Create a Kafka configuration object
    conf = rd_kafka_conf_new();

    // Set the bootstrap.servers property to connect to the Kafka broker
    if (rd_kafka_conf_set(conf, "bootstrap.servers", KAFKA_BROKER, errstr, sizeof(errstr)) != RD_KAFKA_CONF_OK) {
        fprintf(stderr, "Error setting Kafka broker configuration: %s\n", errstr);
        return 1;
    }

    // Create the Kafka producer
    rk = rd_kafka_new(RD_KAFKA_PRODUCER, conf, errstr, sizeof(errstr));
    if (!rk) {
        fprintf(stderr, "Failed to create Kafka producer: %s\n", errstr);
        return 1;
    }

    // Get the topic handle
    rd_kafka_topic_t *rkt = rd_kafka_topic_new(rk, KAFKA_TOPIC, NULL);

    // Message payload and size
    const char *payload = "Hello, World!";
    size_t len = strlen(payload);

    // Send the message
    if (rd_kafka_produce(rkt, RD_KAFKA_PARTITION_UA, RD_KAFKA_MSG_F_COPY,
                         (void *)payload, len, NULL, 0, NULL) != 0) {
        fprintf(stderr, "Failed to send message: %s\n", rd_kafka_err2str(rd_kafka_last_error()));
        return 1;
    }

    // Wait for messages to be sent (asynchronously)
    rd_kafka_flush(rk, 10 * 1000);

    // Destroy Kafka topic handle
    rd_kafka_topic_destroy(rkt);

    // Destroy Kafka producer instance
    rd_kafka_destroy(rk);

    return 0;
}
