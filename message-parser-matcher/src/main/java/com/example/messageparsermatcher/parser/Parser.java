package com.example.messageparsermatcher.parser;

import com.example.messageparsermatcher.parser.dto.ParsedResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static final Pattern USER_PATTERN =
            Pattern.compile("@\\w+");

    private static final Pattern LINE_PRODUCT_PRICE_PATTERN =
            Pattern.compile("^(.+?)[\\s\\-–—]+(\\d[\\d\\s.,]*)(?:\\s*(тенге|тг|₸))?$",
                    Pattern.CASE_INSENSITIVE);

    private static final Pattern PRICE_ONLY_PATTERN =
            Pattern.compile("(\\d[\\d\\s.,]*\\d|\\d)\\s*(тенге|тг|₸)?",
                    Pattern.CASE_INSENSITIVE);

    public static List<ParsedResult> parse(String text, String channelUsername) {
        List<ParsedResult> result = new ArrayList<>();

        String salesman = findSalesman(text);

        if (salesman == null) {
            salesman = channelUsername;
        }

        String[] lines = text.split("\\R");

        for (String line : lines) {
            line = line.trim();

            if (line.isBlank()) {
                continue;
            }

            Matcher matcher = LINE_PRODUCT_PRICE_PATTERN.matcher(line);

            if (matcher.find()) {
                String product = matcher.group(1).trim();
                BigDecimal price = parsePrice(matcher.group(2));

                if (price != null) {
                    result.add(new ParsedResult(product, price, salesman));
                }
            }
        }

        if (!result.isEmpty()) {
            return result;
        }

        String product = lines.length > 0 ? lines[0].trim() : null;
        BigDecimal price = findFirstPrice(text);

        if (product != null && price != null) {
            result.add(new ParsedResult(product, price, salesman));
        }

        return result;
    }

    private static String findSalesman(String text) {
        Matcher matcher = USER_PATTERN.matcher(text);

        if (matcher.find()) {
            return matcher.group();
        }

        return null;
    }

    private static BigDecimal findFirstPrice(String text) {
        Matcher matcher = PRICE_ONLY_PATTERN.matcher(text);

        if (matcher.find()) {
            return parsePrice(matcher.group(1));
        }

        return null;
    }

    private static BigDecimal parsePrice(String raw) {
        if (raw == null) {
            return null;
        }

        raw = raw
                .replaceAll("[\\s.,]", "");

        try {
            return new BigDecimal(raw);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}