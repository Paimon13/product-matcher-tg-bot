package com.example.messageparsermatcher.product.service.impl;

import com.example.messageparsermatcher.kafka.KafkaProducer;
import com.example.messageparsermatcher.matcher.Matcher;
import com.example.messageparsermatcher.matcher.dto.MatchDto;
import com.example.messageparsermatcher.parser.Parser;
import com.example.messageparsermatcher.parser.dto.ParsedResult;
import com.example.messageparsermatcher.product.dto.ProductDto;
import com.example.messageparsermatcher.kafka.dto.TgMessageDto;
import com.example.messageparsermatcher.product.model.Product;
import com.example.messageparsermatcher.product.repository.ProductRepository;
import com.example.messageparsermatcher.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final Matcher matcher;
    private final KafkaProducer kafkaProducer;

    public ProductServiceImpl(ProductRepository productRepository, Matcher matcher, KafkaProducer kafkaProducer) {
        this.productRepository = productRepository;
        this.matcher = matcher;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void saveProduct(ProductDto dto) {
        try {
            Product product = new Product(
                    dto.getChatId(),
                    dto.getMessageId(),
                    dto.getChannel(),
                    dto.getProduct(),
                    dto.getPrice(),
                    dto.getCurrency(),
                    dto.getSalesman(),
                    dto.getTimestamp()
            );

            Product saved = productRepository.save(product);
            System.out.println("✅ Saved to Mongo! ID: " + saved.getId());
        }catch (Exception e) {
            System.err.println("❌ SAVE ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void searchMatchOrSave(TgMessageDto dto) {
        List<ParsedResult> parsedResults = Parser.parse(dto.getText(), dto.getChannel());

        for (ParsedResult parsedResult : parsedResults) {
            if(parsedResult.getSalesman().isEmpty()){
                parsedResult.setSalesman(dto.getChannel());
            }

            ProductDto productDto = new ProductDto(
                    dto.getChatId(),
                    dto.getMessageId(),
                    dto.getChannel(),
                    parsedResult.getProduct(),
                    parsedResult.getPrice(),
                    parsedResult.getCurrency(),
                    parsedResult.getSalesman(),
                    dto.getDate()
            );
            saveProduct(productDto);

            List<MatchDto> matchDtoList = matcher.searchMatchWithProduct(productDto);


            if(!matchDtoList.isEmpty()) {
                for (MatchDto matchDto : matchDtoList) {
                    kafkaProducer.send(matchDto);
                }
            }
        }
    }
}
