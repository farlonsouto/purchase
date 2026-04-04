package org.farlon.kafka.consumer.consumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleConsumer {
  private static final Logger log = LoggerFactory.getLogger(SimpleConsumer.class);

  // Use AtomicBoolean to safely share the state between threads
  private static final AtomicBoolean keepRunning = new AtomicBoolean(true);

  public static void main(String[] args) {
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "farlon-learning");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {

      final Thread mainThread = Thread.currentThread();
      Runtime.getRuntime()
          .addShutdownHook(
              new Thread(
                  () -> {
                    log.warn("Shutdown signal detected.");
                    keepRunning.set(false); // 1. Signal the loop to stop
                    consumer.wakeup(); // 2. Interrupt the current poll()
                    try {
                      mainThread.join();
                    } catch (InterruptedException e) {
                      log.error("Shutdown hook interrupted", e);
                    }
                  }));

      consumer.subscribe(Collections.singletonList("test-topic"));

      // 3. The loop now has a proper exit condition
      while (keepRunning.get()) {
        try {
          ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
          for (ConsumerRecord<String, String> record : records) {
            log.info(
                "Received: key={}, value={}, offset={}",
                record.key(),
                record.value(),
                record.offset());
          }
        } catch (WakeupException e) {
          // Check if we should stop; if keepRunning is false, the loop ends naturally
          if (keepRunning.get()) {
            log.error("Unexpected wakeup call", e);
            throw e;
          }
        }
      }
      log.info("Loop exited naturally.");

    } catch (Exception e) {
      log.error("Unexpected error: {}", e.getMessage(), e);
    } finally {
      log.info("The consumer is now safely closed.");
    }
  }
}
