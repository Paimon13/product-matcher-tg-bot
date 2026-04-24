package com.example.messageparsermatcher.schedule;

import com.example.messageparsermatcher.product.model.Product;
import com.example.messageparsermatcher.product.repository.ProductRepository;
import com.example.messageparsermatcher.searchquery.model.SearchQuery;
import com.example.messageparsermatcher.searchquery.repository.SearchQueryRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class Clear {
    private final ProductRepository productRepository;
    private final SearchQueryRepository searchQueryRepository;


    public Clear(ProductRepository productRepository, SearchQueryRepository searchQueryRepository) {
        this.productRepository = productRepository;
        this.searchQueryRepository = searchQueryRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void clear() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(14);

        List<Product> products = productRepository.findByTimestampLessThanEqual(threshold);
        List<SearchQuery> searchQueries = searchQueryRepository.findByCreatedAtLessThanEqual(threshold);

        productRepository.deleteAll(products);
        searchQueryRepository.deleteAll(searchQueries);
    }
}
