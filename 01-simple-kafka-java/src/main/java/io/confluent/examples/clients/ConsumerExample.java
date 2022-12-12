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

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerExample {

  public static void main(final String[] args) throws Exception {

    final String topic = "test.topic.4";


    final Properties props = new Properties();

    // Add additional properties.
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-new-consumer-5");
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_DOC, "earliest");
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 6500);
    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 6000);



    final Consumer<String, String> consumer = new KafkaConsumer<String, String>(props);
    consumer.subscribe(Arrays.asList(topic));

    Long total_count = 0L;

    try {
      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        //consumer.pause(consumer.assignment());
        for (ConsumerRecord<String, String> record : records) {

          records.partitions().forEach(cons-> cons.partition().);
          System.out.println(records.partitions().toString());
          String key = record.key();
          String value = record.value();

          total_count++;
          System.out.printf("Consumed record with key %s and value %s, and updated total count to %d%n", key, value, total_count);
        }

        try {Thread.sleep(10000);} catch(Exception ex) {}
        //consumer.resume(consumer.assignment());
      }
    } finally {

      consumer.close();
    }
  }
}
