package io.confluent.examples.clients.basicavro;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.common.errors.SerializationException;

import java.util.Properties;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileInputStream;
import java.io.InputStream;

public class ProducerExample {

    private static final String TOPIC = "transaction.payment";
    private static final Properties props = new Properties();

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(final String[] args) throws IOException {
      props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "pkc-ymrq7.us-east-2.aws.confluent.cloud:9092");
        props.put("security.protocol","SASL_SSL" );
        props.put("sasl.mechanism", "PLAIN");
        props.put("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username='7BO7URVDMYS6VPQU' password='lmmPByIqOlsuWv5eaveLZxidnITjxAdwAqBVzGopgTRasYWP6WYTQy6rTGDHzkcD';" );
      props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "https://psrc-95km5.eu-central-1.aws.confluent.cloud");
        props.put("basic.auth.credentials.source","USER_INFO");
        props.put("basic.auth.user.info","FO7SDDHXH2YJ2AKK:iZz2fU+xmlQASU/fzv6f3kZKdwlSOJL1tpF4aEUKWYRIvfgIfEWNZZoMOq2ITpvA");
      props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);

        try (KafkaProducer<String, Payment> producer = new KafkaProducer<String, Payment>(props)) {
            for (long i = 0; i < 10; i++) {
                final String orderId = "id" + Long.toString(i);
                final Payment payment = new Payment(orderId, 1000.00d);
                final ProducerRecord<String, Payment> record = new ProducerRecord<String, Payment>(TOPIC, payment.getId().toString(), payment);
                producer.send(record, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        if (e != null)
                           e.printStackTrace();
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
