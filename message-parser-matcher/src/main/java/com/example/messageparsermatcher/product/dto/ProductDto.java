package com.example.messageparsermatcher.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private long chatId;
    private long messageId;
    private String channel;
    private String product;
    private BigDecimal price;
    private String currency;
    private String salesman;
    private LocalDateTime timestamp;
}
