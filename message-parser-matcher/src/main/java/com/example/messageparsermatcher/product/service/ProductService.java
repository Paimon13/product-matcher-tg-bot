package com.example.messageparsermatcher.product.service;

import com.example.messageparsermatcher.product.dto.ProductDto;
import com.example.messageparsermatcher.kafka.dto.TgMessageDto;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    void saveProduct(ProductDto dto);

    void searchMatchOrSave(TgMessageDto dto);
}
