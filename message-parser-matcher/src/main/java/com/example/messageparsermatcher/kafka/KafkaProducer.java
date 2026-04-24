package com.example.messageparsermatcher.kafka;

import com.example.messageparsermatcher.matcher.dto.MatchDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, MatchDto> kafkaTemplate;

    @Value("${kafka.topic.telegram}")
    private String topic;

    public KafkaProducer(KafkaTemplate<String, MatchDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(MatchDto dto) {
        kafkaTemplate.send(topic, String.valueOf(dto.getChatId()), dto);
    }
}