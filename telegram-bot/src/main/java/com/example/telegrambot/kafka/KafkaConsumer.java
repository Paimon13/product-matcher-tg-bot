package com.example.telegrambot.kafka;

import com.example.telegrambot.bot.ProductBot;
import com.example.telegrambot.bot.dto.MatchDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private final ProductBot productBot;

    public KafkaConsumer(ProductBot productBot) {
        this.productBot = productBot;
    }

    @KafkaListener(topics = "match-topic", groupId = "group1")
    public void consumeSearch(MatchDto dto) {
        System.out.println("Received Message: " + dto);
        String text = "Кукла: " + dto.getProduct() + " найдена!\n"+
                "Цена:  " + dto.getPrice() + " тенге\n" +
                "Продавец: " + dto.getSalesman() +
                "\nСсылка на сообщение: \nhttps://t.me/" + dto.getChannel().substring(1) + "/" + dto.getMessageId();
        productBot.sendMessage(dto.getChatId(), text);
    }
}
