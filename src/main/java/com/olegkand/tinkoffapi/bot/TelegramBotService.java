package com.olegkand.tinkoffapi.bot;

import com.olegkand.tinkoffapi.model.Currency;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TelegramBotService implements BotService{

    String ACCOUNT_BROKER = "2041834157";

    @Override
    public String getTotalPortfolio() {
        return  null;
    }

}
