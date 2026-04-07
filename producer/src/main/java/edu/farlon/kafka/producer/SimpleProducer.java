package edu.farlon.kafka.producer;

import java.util.Properties;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleProducer {

  private static final Logger logger = LoggerFactory.getLogger(SimpleProducer.class);

  public static void main(String[] args) {
    Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    KafkaProducer<String, String> producer = new KafkaProducer<>(props);

    // Topic name, Key, Value
    ProducerRecord<String, String> record =
        new ProducerRecord<>("test-topic", "key1", "Hello Java!");

    producer.send(
        record,
        (metadata, exception) -> {
          if (exception == null) {
            logger.info("Sent to: {} | Offset: {}", metadata.topic(), metadata.offset());
          }
        });

    producer.flush();
    producer.close();
  }
}
