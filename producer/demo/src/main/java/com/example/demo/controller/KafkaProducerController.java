package com.example.demo.controller;

import com.example.demo.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka")
public class KafkaProducerController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping("/generate/single")
    public ResponseEntity<String> generateSingleMessage() {
        kafkaProducerService.generateAndSendUserEvent();
        return ResponseEntity.ok("Single message generated and sent successfully");
    }

    @PostMapping("/generate/multiple/{count}")
    public ResponseEntity<String> generateMultipleMessages(@PathVariable int count) {
        if (count <= 0 || count > 100) {
            return ResponseEntity.badRequest().body("Count must be between 1 and 100");
        }
        kafkaProducerService.generateMultipleEvents(count);
        return ResponseEntity.ok(String.format("%d messages generated and sent successfully", count));
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendCustomMessage(
            @RequestParam(required = false) String key,
            @RequestParam String message) {
        if (key != null) {
            kafkaProducerService.sendMessageWithKey(key, message);
        } else {
            kafkaProducerService.sendMessageWithoutKey(message);
        }
        return ResponseEntity.ok("Custom message sent successfully");
    }

}
