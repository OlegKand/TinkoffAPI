package com.olegkand.tinkoffapi;

import com.olegkand.tinkoffapi.bot.Bot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TinkoffTelegramBot {

    public static void main(String[] args) {
        SpringApplication.run(TinkoffTelegramBot.class, args);
    }
}
