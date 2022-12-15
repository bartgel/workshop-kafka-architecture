/**
 * Copyright 2020 Confluent Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.confluent.examples.clients;


import org.apache.kafka.clients.producer.*;

import java.io.IOException;
import java.util.Properties;


public class ProducerFalseExample {

  public static void main(final String[] args) throws IOException {

    final Properties props = new Properties();

    // Create topic if needed
    final String topic = "test.topic";

    // Add additional properties.
    props.put(ProducerConfig.ACKS_CONFIG, "-1");
    props.put(ProducerConfig.RETRIES_CONFIG, 1);
    props.put(ProducerConfig.LINGER_MS_CONFIG, 10000);
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, 10000);
    props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, RoundRobinPartitioner.class.getName());
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");


    Producer<String, String> producer = new KafkaProducer<String, String>(props);

    // Produce sample data
    while (true) {
      final Long numMessages = 20L;
      for (Long i = 0L; i < numMessages; i++) {
        String key = "alice";
        String record = "asdfdsafads" + String.valueOf(i);

        System.out.printf("Producing record: %s\t%s%n", key, record);
        try {
          RecordMetadata record2 = producer.send(new ProducerRecord<String, String>(topic, record)).get();
          // have some fun with this record.
        } catch (Exception ex) {
          ex.printStackTrace();;
        }
      }
    }

    /*producer.flush();

    System.out.printf("10 messages were produced to topic %s%n", topic);

    producer.close();*/
  }
}
