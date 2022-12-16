package io.confluent.examples.clients.json;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaJsonSerializer;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer;
import io.confluent.kafka.streams.serdes.json.KafkaJsonSchemaSerde;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.errors.SerializationException;

import java.util.Properties;
import java.io.IOException;
import java.util.UUID;

public class ProducerExample {

    private static final String TOPIC = "transaction.json";
    private static final Properties props = new Properties();

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(final String[] args) throws IOException {

      props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
      props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
      props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSchemaSerializer.class);

        try (KafkaProducer<String, User> producer = new KafkaProducer<String, User>(props)) {
            for (long i = 0; i < 10; i++) {
                final String orderId = "id" + Long.toString(i);
                final User payment = User.builder().age(30).firstName("Jan").lastName("Test") .build();
                final ProducerRecord<String, User> record = new ProducerRecord<String, User>(TOPIC, UUID.randomUUID().toString(), payment);
                producer.send(record, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        // Do error handling
                        //e.printStackTrace();
                    }
                });
            }

            producer.flush();
            System.out.printf("Successfully produced 10 messages to a topic called %s%n", TOPIC);

        } catch (final SerializationException e) {
            e.printStackTrace();
        }
    }
}
