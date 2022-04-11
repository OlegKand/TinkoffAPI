package com.olegkand.tinkoffapi;

import com.olegkand.tinkoffapi.bot.Bot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties
public class TinkoffTelegramBot {

    public static void main(String[] args) {
        SpringApplication.run(TinkoffTelegramBot.class, args);
    }
}
