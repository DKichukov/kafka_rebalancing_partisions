//package com.example.demo.service;
//
//import jakarta.annotation.PostConstruct;
//import org.apache.kafka.clients.admin.AdminClient;
//import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaAdmin;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//import java.util.concurrent.ExecutionException;
//
//@Service
//public class KafkaProducerService {
//
//    private static final String TOPIC_NAME = "user-event";
//
//    private static final int PARTITION_COUNT = 10;
//
//    private static final short REPLICATION_FACTOR = 1;  // Set to 1 for local development
//
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    @Autowired
//    private KafkaAdmin kafkaAdmin;
//
//    @PostConstruct
//    public void createTopic() {
//        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
//            // Check if topic exists
//            if (!adminClient.listTopics().names().get().contains(TOPIC_NAME)) {
//                // Create new topic
//                NewTopic newTopic = new NewTopic(TOPIC_NAME, PARTITION_COUNT, REPLICATION_FACTOR);
//                adminClient.createTopics(Collections.singleton(newTopic)).all().get();
//                System.out.println("Topic " + TOPIC_NAME + " created successfully");
//            }
//        } catch (ExecutionException | InterruptedException e) {
//            throw new RuntimeException("Failed to create topic", e);
//        }
//    }
//
//    // Method to send message with key
//    public void sendMessageWithKey(String key, String message) {
//        try {
//            kafkaTemplate.send(TOPIC_NAME, key, message)
//                    .whenComplete((result, ex) -> {
//                        if (ex == null) {
//                            System.out.println("Message sent successfully with key: " + key +
//                                    " to partition: " + result.getRecordMetadata().partition());
//                        } else {
//                            System.err.println("Failed to send message with key: " + key);
//                            ex.printStackTrace();
//                        }
//                    });
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to send message with key", e);
//        }
//    }
//
//    // Method to send message without key
//    public void sendMessageWithoutKey(String message) {
//        try {
//            kafkaTemplate.send(TOPIC_NAME, message)
//                    .whenComplete((result, ex) -> {
//                        if (ex == null) {
//                            System.out.println("Message sent successfully to partition: " +
//                                    result.getRecordMetadata().partition());
//                        } else {
//                            System.err.println("Failed to send message");
//                            ex.printStackTrace();
//                        }
//                    });
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to send message", e);
//        }
//    }
//
//}
