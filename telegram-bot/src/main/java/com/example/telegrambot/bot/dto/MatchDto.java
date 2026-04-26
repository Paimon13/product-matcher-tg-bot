package com.example.telegrambot.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchDto {
    private Long chatId;
    private Long messageId;
    private String channel;
    private String product;
    private String salesman;
    private BigDecimal price;
    private String currency;
    private LocalDateTime timestamp;
}