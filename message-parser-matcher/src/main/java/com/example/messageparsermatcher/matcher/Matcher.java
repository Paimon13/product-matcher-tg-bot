package com.example.messageparsermatcher.matcher;

import com.example.messageparsermatcher.matcher.dto.MatchDto;
import com.example.messageparsermatcher.product.dto.ProductDto;
import com.example.messageparsermatcher.product.model.Product;
import com.example.messageparsermatcher.product.repository.ProductRepository;
import com.example.messageparsermatcher.searchquery.dto.SearchQueryDto;
import com.example.messageparsermatcher.searchquery.model.SearchQuery;
import com.example.messageparsermatcher.searchquery.repository.SearchQueryRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class Matcher {
    private final ProductRepository productRepository;
    private final SearchQueryRepository searchQueryRepository;

    public Matcher(ProductRepository productRepository, SearchQueryRepository searchQueryRepository) {
        this.productRepository = productRepository;
        this.searchQueryRepository = searchQueryRepository;
    }

    public MatchDto searchMatchWithQuery(SearchQueryDto dto) {
        List<Product> productList =
                productRepository.findMatches(
                        dto.getProduct().trim(),
                        dto.getMaxPrice(),
                        dto.getCurrency()
                );
        Product bestProduct = productList
                .stream()
                .min(Comparator.comparing(Product::getPrice))
                .orElse(null);


        if (bestProduct == null) {
            System.out.println("match not found for: " + dto.getProduct());
            return null;
        }

        return new MatchDto(
                dto.getChatId(),
                bestProduct.getMessageId(),
                bestProduct.getChannel(),
                bestProduct.getProduct(),
                bestProduct.getSalesman(),
                bestProduct.getPrice(),
                bestProduct.getCurrency(),
                bestProduct.getTimestamp()
        );
    }
    public List<MatchDto> searchMatchWithProduct(ProductDto dto) {
        List<SearchQuery> queryList =
                searchQueryRepository.findMatches(
                        dto.getProduct().trim(),
                        dto.getPrice(),
                        dto.getCurrency()
                );

        return queryList.stream()
                .map(searchQuery -> new MatchDto(
                        searchQuery.getChatId(),
                        dto.getMessageId(),
                        dto.getChannel(),
                        searchQuery.getProduct(),
                        dto.getSalesman(),
                        dto.getPrice(),
                        dto.getCurrency(),
                        dto.getTimestamp()
                ))
                .toList();
    }
}
