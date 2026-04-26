package com.example.messageparsermatcher.searchquery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "search_queries")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchQuery {
    @Id
    private String id;

    private Long chatId;
    private Long userId;
    private String product;
    private BigDecimal maxPrice;
    private String currency;
    private LocalDateTime createdAt;

    public SearchQuery(Long chatId, Long userId, String product, BigDecimal maxPrice,String currency,  LocalDateTime createdAt){
        this.chatId = chatId;
        this.userId = userId;
        this.product = product;
        this.maxPrice = maxPrice;
        this.currency = currency;
        this.createdAt = createdAt;
    }
}