package com.example.telegrambot.bot.model;

import com.example.telegrambot.bot.enums.UserState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {
    private UserState state = UserState.IDLE;
    private String productName;
    private BigDecimal maxPrice;
}