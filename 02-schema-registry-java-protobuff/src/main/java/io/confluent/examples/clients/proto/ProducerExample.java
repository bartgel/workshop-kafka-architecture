package io.confluent.examples.clients.proto;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.protobuf.KafkaProtobufSerializer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.errors.SerializationException;

import java.util.Properties;
import java.io.IOException;
import java.util.UUID;

public class ProducerExample {

    private static final String TOPIC = "transaction.simple";
    private static final Properties props = new Properties();

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(final String[] args) throws IOException {

      props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
      props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
      props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaProtobufSerializer.class);

        try (KafkaProducer<String, SimpleMessageOuterClass.SimpleMessage> producer = new KafkaProducer<String, SimpleMessageOuterClass.SimpleMessage>(props)) {
            for (long i = 0; i < 10; i++) {
                final String orderId = "id" + Long.toString(i);
                final SimpleMessageOuterClass.SimpleMessage payment =  SimpleMessageOuterClass.SimpleMessage.newBuilder()
                        .setContent("aaa")
                        .setDateTime("jk")
                        .build();
                final ProducerRecord<String, SimpleMessageOuterClass.SimpleMessage> record
                        = new ProducerRecord<String, SimpleMessageOuterClass.SimpleMessage>(TOPIC, UUID.randomUUID().toString(), payment);
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
