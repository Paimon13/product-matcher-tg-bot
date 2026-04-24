package com.example.messageparsermatcher.product.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "product")
@Data
public class Product {
    @Id
    private String id;

    private Long channelId;
    private Long messageId;
    private String channel;
    private String product;
    private BigDecimal price;
    private String salesman;
    private LocalDateTime timestamp;

    public Product(Long channelId, Long messageId, String channel, String product, BigDecimal price, String salesman, LocalDateTime timestamp) {
        this.channelId = channelId;
        this.messageId = messageId;
        this.channel = channel;
        this.product = product;
        this.price = price;
        this.salesman = salesman;
        this.timestamp = timestamp;
    }
}
