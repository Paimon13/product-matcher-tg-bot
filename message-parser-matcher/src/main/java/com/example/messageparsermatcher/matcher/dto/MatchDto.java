package com.example.messageparsermatcher.matcher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
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
