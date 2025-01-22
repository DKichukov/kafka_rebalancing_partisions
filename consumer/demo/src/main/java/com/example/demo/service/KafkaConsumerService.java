package com.example.demo.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    private static final String TOPIC_NAME = "user-event";
    private static final String GROUP_ID = "event-consumers";

    @KafkaListener(
            topics = TOPIC_NAME,
            groupId = GROUP_ID,
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        try {
            // Log the received message details
            logger.info("Received message: \n" +
                            "Topic: {} \n" +
                            "Partition: {} \n" +
                            "Offset: {} \n" +
                            "Key: {} \n" +
                            "Value: {}",
                    record.topic(),
                    record.partition(),
                    record.offset(),
                    record.key(),
                    record.value()
            );

            // Process the message here
            processMessage(record);

            // Manually acknowledge the message
            acknowledgment.acknowledge();

        } catch (Exception e) {
            logger.error("Error processing message: {}", e.getMessage(), e);
            // You might want to implement retry logic or dead letter queue here
        }
    }

    private void processMessage(ConsumerRecord<String, String> record) {
        // Add your message processing logic here
        logger.info("Processing message: {}", record.value());
        // Simulate some processing time
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
