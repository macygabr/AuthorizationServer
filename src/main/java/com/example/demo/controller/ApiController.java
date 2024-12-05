package com.example.demo.controller;

import com.example.demo.service.kafka.KafkaProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class ApiController {
    private final KafkaProducerService kafkaProducer;

    @Autowired
    public ApiController(KafkaProducerService kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @Scheduled(fixedRate = 1000*60*5)
    public void sendKafkaMessage() throws JsonProcessingException {
        kafkaProducer.sendMessage("auth_response", "0" ,"reboot");
    }
}
