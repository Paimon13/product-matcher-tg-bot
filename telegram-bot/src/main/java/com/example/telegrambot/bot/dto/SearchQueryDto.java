package com.example.telegrambot.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchQueryDto {
    private Long chatId;
    private Long userId;
    private String product;
    private BigDecimal maxPrice;
    private String currency;
}