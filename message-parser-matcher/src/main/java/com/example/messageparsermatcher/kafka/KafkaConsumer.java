package com.example.messageparsermatcher.kafka;

import com.example.messageparsermatcher.product.dto.TgMessageDto;
import com.example.messageparsermatcher.product.service.ProductService;
import com.example.messageparsermatcher.searchquery.dto.SearchQueryDto;
import com.example.messageparsermatcher.searchquery.service.SearchQueryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private final ProductService productService;
    private final SearchQueryService searchQueryService;

    public KafkaConsumer(ProductService productService, SearchQueryService searchQueryService) {
        this.productService = productService;
        this.searchQueryService = searchQueryService;
    }

    @KafkaListener(topics = "telegram-message-topic", groupId = "group1")
    public void consume(TgMessageDto dto) {
        System.out.println("Received Message: " + dto);
        productService.searchMatchOrSave(dto);
    }
    @KafkaListener(topics = "search-topic", groupId = "group1")
    public void consumeSearch(SearchQueryDto dto) {
        System.out.println("Received Message: " + dto);
        searchQueryService.searchMatchOrSave(dto);
    }
}
