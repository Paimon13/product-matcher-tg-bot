package com.example.telegrambot.kafka;


import com.example.telegrambot.bot.dto.SearchQueryDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, SearchQueryDto> kafkaTemplate;

    @Value("${kafka.topic.telegram}")
    private String topic;

    public KafkaProducer(KafkaTemplate<String, SearchQueryDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(SearchQueryDto dto) {
        kafkaTemplate.send(topic, String.valueOf(dto.getChatId()), dto);
    }
}