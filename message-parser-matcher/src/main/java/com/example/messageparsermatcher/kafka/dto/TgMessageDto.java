package com.example.messageparsermatcher.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TgMessageDto {
    private long chatId;
    private long messageId;
    private String channel;
    private String text;
    private LocalDateTime date;
}