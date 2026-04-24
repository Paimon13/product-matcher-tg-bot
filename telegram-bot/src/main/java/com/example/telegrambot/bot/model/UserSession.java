package com.example.telegrambot.bot.model;

import com.example.telegrambot.bot.enums.UserState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {
    private UserState state = UserState.IDLE;
    private String productName;
}