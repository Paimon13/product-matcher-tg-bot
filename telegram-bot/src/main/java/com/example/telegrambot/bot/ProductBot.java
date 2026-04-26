package com.example.telegrambot.bot;

import com.example.telegrambot.bot.dto.SearchQueryDto;
import com.example.telegrambot.bot.enums.UserState;
import com.example.telegrambot.bot.model.UserSession;
import com.example.telegrambot.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProductBot extends TelegramLongPollingBot {

    private final String botToken;

    private final Map<Long, UserSession> sessions = new ConcurrentHashMap<>();

    private final KafkaProducer producer;


    public ProductBot(@Value("${telegram.bot.token}") String botToken, KafkaProducer producer) {
        this.botToken = botToken;
        this.producer = producer;
    }

    @Override
    public String getBotUsername() {
        return "YOUR_BOT_USERNAME";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        Long chatId = update.getMessage().getChatId();
        Long userId = update.getMessage().getFrom().getId();
        String text = update.getMessage().getText().trim();

        UserSession session = sessions.computeIfAbsent(chatId, id -> new UserSession());

        if (text.equals("/start")) {
            session.setState(UserState.IDLE);
            session.setProductName(null);
            sendMessage(chatId, "Hello!\nPrint /add to add a product");
            return;
        }

        if (text.equals("/add")) {
            session.setState(UserState.WAITING_PRODUCT_NAME);
            session.setProductName(null);
            sendMessage(chatId, "Enter product name");
            return;
        }

        switch (session.getState()) {
            case WAITING_PRODUCT_NAME -> {
                session.setProductName(text);
                session.setState(UserState.WAITING_PRODUCT_PRICE);
                sendMessage(chatId, "Now enter product max price");
            }

            case WAITING_PRODUCT_PRICE -> {
                try {
                    session.setMaxPrice(BigDecimal.valueOf(Double.parseDouble(text)));

                    session.setState(UserState.WAITING_PRODUCT_PRICE);
                    sendMessage(chatId, "Now enter your currency (KZT, RUB, USD, EUR)");

                } catch (NumberFormatException e) {
                    sendMessage(chatId, "The price must be a number, example: 1500");
                }
            }
            case WAITING_PRODUCT_CURRENCY -> {
                try {
                    String currency = text;
                    String productName = session.getProductName();
                    BigDecimal maxPrice = session.getMaxPrice();

                    SearchQueryDto  searchQueryDto = new SearchQueryDto(
                            chatId,
                            userId,
                            productName,
                            maxPrice,
                            currency
                    );
                    producer.send(searchQueryDto);
                    sendMessage(chatId,
                            "Product is save\n" +
                                    "Name: " + productName + "\n" +
                                    "Price: " + maxPrice + currency);

                    session.setState(UserState.IDLE);
                    session.setProductName(null);

                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Currency must be one of 'KZT', 'RUB', 'USD', 'EUR'");
                }
            }


            default -> sendMessage(chatId, "Print /add to add a product");
        }
    }

    public void sendMessage(Long chatId, String text) {
        try {
            execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(text)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}