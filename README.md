# Kafka Producer-Consumer Demo Project

This project demonstrates a Kafka producer-consumer implementation using Spring Boot, showcasing partition management and consumer group rebalancing.

## Prerequisites

- Java 21
- Docker and Docker Compose
- Maven

## Project Structure

The project consists of two main components:
- **Kafka Producer**: Creates and sends messages to a Kafka topic
- **Kafka Consumer**: Receives and processes messages from the topic

## Technology Stack

- Spring Boot
- Apache Kafka
- Docker
- Spring Kafka
- Spring Web

## Getting Started

### 1. Start Kafka Environment

First, start the Kafka environment using Docker Compose:

```bash
docker-compose up -d
```

This will start:
- Zookeeper (port 2181)
- Kafka (ports 9092, 9093)
- Kafdrop - Kafka UI (port 9000)

You can access Kafdrop at http://localhost:9000 to monitor your Kafka cluster.

### 2. Run the Producer

```bash
# From the producer directory
./mvnw spring-boot:run
```

The producer will:
- Create a topic named 'user-event' with 10 partitions
- Expose REST endpoints for sending messages

### 3. Run Multiple Consumers

Run multiple instances of the consumer to observe partition rebalancing:

```bash
# From the consumer directory
# First instance
./mvnw spring-boot:run

# Second instance (in a new terminal)
./mvnw spring-boot:run

# Add more instances as needed
```

## API Endpoints

### Producer Endpoints

```bash
# Generate a single message
POST http://localhost:8080/api/kafka/generate/single

# Generate multiple messages
POST http://localhost:8080/api/kafka/generate/multiple/{count}

# Send custom message
POST http://localhost:8080/api/kafka/send?key={key}&message={message}
```

## Configuration

### Producer Configuration (application.yaml)
```yaml
spring:
  application:
    name: kafka-producer
  kafka:
    bootstrap-servers: localhost:9093
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
    listener:
      ack-mode: manual
```

### Consumer Configuration (application.yaml)
```yaml
spring:
  application:
    name: kafka-consumer
  kafka:
    bootstrap-servers: localhost:9093
    consumer:
      group-id: event-consumers
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      enable-auto-commit: false
      max-poll-records: 10
      properties:
        session.timeout.ms: 10000
        heartbeat.interval.ms: 3000
        listener.ack-mode: manual
server:
  port: 0
```

## Key Features

- Topic creation with multiple partitions
- Message sending with and without keys
- Consumer group management
- Partition rebalancing
- Manual acknowledgment
- Concurrent message processing

## Testing Rebalancing

1. Start the producer
2. Start multiple consumer instances
3. Observe the log output showing partition assignments
4. Stop/start consumer instances to see rebalancing in action

## Monitoring

Use Kafdrop (http://localhost:9000) to:
- View topic details
- Monitor partitions
- Track consumer groups
- Inspect messages

## Troubleshooting

If you encounter issues:

1. Ensure Docker services are running:
```bash
docker-compose ps
```

2. Check Kafka logs:
```bash
docker logs kafka
```

3. Verify Kafdrop is accessible at http://localhost:9000

4. Ensure correct ports are free and accessible:
- 2181 (Zookeeper)
- 9092, 9093 (Kafka)
- 9000 (Kafdrop)
