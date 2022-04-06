package com.olegkand.tinkoffapi.bot;

import com.olegkand.tinkoffapi.model.Currency;
import com.olegkand.tinkoffapi.service.TinkoffPortfolioService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TelegramBotService implements BotService{

    String ACCOUNT_BROKER = "2041834157";

    private final TinkoffPortfolioService tinkoffPortfolioService;

    public TelegramBotService(TinkoffPortfolioService tinkoffPortfolioService) {
        this.tinkoffPortfolioService = tinkoffPortfolioService;
    }

    @Override
    public String getTotalPortfolio() {
        Map<Currency, Double> doubleMap = tinkoffPortfolioService.getPortfolioValue(ACCOUNT_BROKER);

        return "Общий доход портфеля: \t" + String.format("%.2f",doubleMap.get(Currency.TOTAL_RUB)) + " RUB\n" +
               "Доход по иностранным активам : " + String.format("%.2f",doubleMap.get(Currency.USD)) +" USD\n" +
               "Доход по российским активам  : " + String.format("%.2f",doubleMap.get(Currency.RUB)) +" RUB";
    }

}
