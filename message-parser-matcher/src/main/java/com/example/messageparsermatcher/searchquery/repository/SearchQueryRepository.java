package com.example.messageparsermatcher.searchquery.repository;


import com.example.messageparsermatcher.searchquery.model.SearchQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface SearchQueryRepository extends MongoRepository<SearchQuery, String> {
    @Query("{ 'product': { $regex: ?0, $options: 'i' }, 'maxPrice': { $gte: ?1 } }")
    List<SearchQuery> findByProductRegexAndMaxPriceGreaterThanEqual(String product, BigDecimal price);

    List<SearchQuery> findByCreatedAtLessThanEqual(LocalDateTime createdAtIsLessThan);
}
