package io.confluent.examples.clients.json;


import io.confluent.kafka.serializers.KafkaJsonDeserializer;
import io.confluent.kafka.serializers.KafkaJsonDeserializerConfig;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.io.IOException;

public class ConsumerExample {

    private static final String TOPIC = "transaction.json";
    private static final Properties props = new Properties();
    private static String configFile;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(final String[] args) throws IOException {
          props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put("schema.registry.url", "http://localhost:8081");

        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-json");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaJsonSchemaDeserializer.class);
        props.put(KafkaJsonDeserializerConfig.JSON_VALUE_TYPE, User.class.getName());

        try (final KafkaConsumer<String, User> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList(TOPIC));

            while (true) {
                final ConsumerRecords<String, User> records = consumer.poll(Duration.ofMillis(100));
                for (final ConsumerRecord<String, User> record : records) {
                    final String key = record.key();
                    final User value = record.value();
                    System.out.printf("key = %s, value = %s%n", key, value);
                }
            }

        }
    }

}
