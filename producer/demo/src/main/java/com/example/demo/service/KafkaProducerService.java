package com.example.demo.service;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaProducerService {

    private static final String TOPIC_NAME = "user-event";

    private static final int PARTITION_COUNT = 10;

    private static final short REPLICATION_FACTOR = 1;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaAdmin kafkaAdmin;

    @PostConstruct
    public void createTopic() {
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            if (!adminClient.listTopics().names().get().contains(TOPIC_NAME)) {
                NewTopic newTopic = new NewTopic(TOPIC_NAME, PARTITION_COUNT, REPLICATION_FACTOR);
                adminClient.createTopics(Collections.singleton(newTopic)).all().get();
                System.out.println("Topic " + TOPIC_NAME + " created successfully");
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to create topic", e);
        }
    }

    // Method to send message with key
    public void sendMessageWithKey(String key, String message) {
        try {
            kafkaTemplate.send(TOPIC_NAME, key, message)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            System.out.println("Message sent successfully with key: " + key +
                                    " to partition: " + result.getRecordMetadata().partition());
                        } else {
                            System.err.println("Failed to send message with key: " + key);
                            ex.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message with key", e);
        }
    }

    // Method to send message without key
    public void sendMessageWithoutKey(String message) {
        try {
            kafkaTemplate.send(TOPIC_NAME, message)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            System.out.println("Message sent successfully to partition: " +
                                    result.getRecordMetadata().partition());
                        } else {
                            System.err.println("Failed to send message");
                            ex.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message", e);
        }
    }

    // Generate and send a random user event
    public void generateAndSendUserEvent() {
        String userId = "user-" + UUID.randomUUID().toString().substring(0, 8);
        String[] events = {"LOGIN", "LOGOUT", "PURCHASE", "VIEW_ITEM", "UPDATE_PROFILE"};
        String event = events[(int) (Math.random() * events.length)];
        String message = String.format("{\"userId\": \"%s\", \"event\": \"%s\", \"timestamp\": \"%s\"}",
                userId, event, System.currentTimeMillis());

        // Randomly decide whether to send with or without key
        if (Math.random() < 0.5) {
            sendMessageWithKey(userId, message);
        } else {
            sendMessageWithoutKey(message);
        }
    }

    // Generate and send multiple events
    public void generateMultipleEvents(int count) {
        for (int i = 0; i < count; i++) {
            generateAndSendUserEvent();
            try {
                Thread.sleep(100); // Add small delay between messages
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

}
