package com.example.messageparsermatcher.parser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class ParsedResult {
    private String product;
    private BigDecimal price;
    private String salesman;
}