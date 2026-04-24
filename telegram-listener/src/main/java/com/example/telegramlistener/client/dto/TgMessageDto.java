package com.example.telegramlistener.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TgMessageDto {
    private long chatId;
    private long messageId;
    private String channel;
    private String text;
    private LocalDateTime date;
}