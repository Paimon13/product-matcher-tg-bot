package com.example.telegramlistener.kafka;

import com.example.telegramlistener.client.dto.TgMessageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, TgMessageDto> kafkaTemplate;

    @Value("${kafka.topic.telegram}")
    private String topic;

    public KafkaProducer(KafkaTemplate<String, TgMessageDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(TgMessageDto dto) {
        kafkaTemplate.send(topic, String.valueOf(dto.getChatId()), dto);
    }
}