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

    private static final String CURRENCY_SYMBOLS = "тенге|тг|₸|руб|р|₽|usd|\\$|eur|€";

    private static final Pattern LINE_PRODUCT_PRICE_PATTERN =
            Pattern.compile("^(.+?)[\\s\\-–—]+(\\d[\\d\\s.,]*)(?:\\s*(" + CURRENCY_SYMBOLS + "))?$",
                    Pattern.CASE_INSENSITIVE);


    public static List<ParsedResult> parse(String text, String channelUsername) {
        List<ParsedResult> result = new ArrayList<>();

        String salesman = findSalesman(text);
        if (salesman == null || salesman.isBlank()) {
            salesman = channelUsername;
        }

        String[] lines = text.split("\\R");

        String currentProduct = null;

        Pattern pricePattern = Pattern.compile(
                "^(\\d[\\d\\s.,]*)(?:\\s*(" + CURRENCY_SYMBOLS + "))?$",
                Pattern.CASE_INSENSITIVE
        );

        for (String line : lines) {
            line = line.trim();
            if (line.isBlank()) continue;

            if (USER_PATTERN.matcher(line).find()) {
                continue;
            }

            Matcher priceMatcher = pricePattern.matcher(line);

            if (priceMatcher.find() && currentProduct != null) {
                BigDecimal price = parsePrice(priceMatcher.group(1));
                String currency = detectCurrency(priceMatcher.group(2));

                if (price != null) {
                    result.add(new ParsedResult(currentProduct, price, currency, salesman));
                }

                currentProduct = null;
            } else {
                currentProduct = line;
            }
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

    private static BigDecimal parsePrice(String raw) {
        if (raw == null) return null;

        raw = raw.trim().replace(" ", "").replace(",", ".");

        if (raw.indexOf('.') != raw.lastIndexOf('.')) {
            raw = raw.replace(".", "");
        }

        try {
            return new BigDecimal(raw);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static String detectCurrency(String raw) {
        if (raw == null) return "KZT";
        String val = raw.toLowerCase().trim();

        if (val.matches("тенге|тг|₸")) return "KZT";
        if (val.matches("руб|р|₽")) return "RUB";
        if (val.matches("usd|\\$")) return "USD";
        if (val.matches("eur|€")) return "EUR";

        return "KZT";
    }
}