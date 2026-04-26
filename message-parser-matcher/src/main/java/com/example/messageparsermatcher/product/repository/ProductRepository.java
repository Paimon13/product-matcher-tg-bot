package com.example.messageparsermatcher.product.repository;


import com.example.messageparsermatcher.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    @Query("{ "+
            " 'product': { $regex: ?0, $options: 'i' }, "+
            " 'price': { $lte: ?1 }, " +
            " 'currency' : ?2" +
            "}")
    List<Product> findMatches(String product, BigDecimal maxPrice, String currency);

    List<Product> findByTimestampLessThanEqual(LocalDateTime timestampIsLessThan);
}


