# Kafka with Java Research Sandbox

A modular Java 17 framework designed for high-performance data streaming and Machine Learning research. This project
implements a clean, fluent dialect for Kafka operations, focusing on experiment reproducibility and stream replay.

## 🚀 Architecture

The project is structured as a Maven Multi-Module system:

    producer: Handles data ingestion into Kafka topics.
    consumer: A robust, signal-aware consumer with graceful shutdown logic.
    core/framework: (In Development) A fluent DSL to replace boilerplate Kafka code with readable logic like Consume.from(topic).into(list).

## 🛠 Tech Stack

    Java 17 (LTS)
    Apache Kafka 3.9.1
    Maven (Dependency Management)
    SLF4J (Structured Logging)
    Docker & Docker Compose (Infrastructure)

## 📦 Security & Performance

    CVE-2025-66566 Fix: The project explicitly excludes vulnerable lz4-java versions and utilizes the patched community fork at.yawk.lz4:1.10.4.
    Resource Management: Implements try-with-resources and AtomicBoolean flags to ensure zero memory leaks and clean partition rebalancing during shutdowns.

### 🚦 Getting Started

1. Start Infrastructure
   Ensure you have Docker installed. This command starts Zookeeper and Kafka in the background.

```bash
docker-compose up -d
```

2. Build the Project

```bash
mvn clean install
```

3. Run the Consumer
   In a dedicated terminal, start the listener:

```bash
mvn exec:java -pl consumer -Dexec.mainClass="org.farlon.kafka.consumer.consumer.SimpleConsumer"
```

4. Run the Producer
   In another terminal, send a test message:

```bash
mvn exec:java -pl producer -Dexec.mainClass="edu.farlon.kafka.producer.SimpleProducer"
```

markdown

### 🧪 Research Features: Time-Travel & Replay

This framework goal is to supports Stream Replay. By leveraging Kafka's offset-to-timestamp mapping, researchers can
rewind the consumer to any specific point in time to re-evaluate ML models against historical data.
java

// Example of the desired Framework Dialect
Consume.from("research-data")
.since(yesterdayTimestamp)
.into(myResearchList);

### 📝 License

MIT - Created for research and educational purposes.
