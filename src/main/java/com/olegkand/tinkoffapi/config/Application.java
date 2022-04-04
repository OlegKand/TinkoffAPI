package com.olegkand.tinkoffapi.config;

import com.olegkand.tinkoffapi.dto.StockPrice;
import com.olegkand.tinkoffapi.exceptions.StockNotFoundException;
import com.olegkand.tinkoffapi.model.StockPortfolio;
import com.olegkand.tinkoffapi.dto.StockPortfolioDto;
import com.olegkand.tinkoffapi.model.Currency;
import org.springframework.scheduling.annotation.Async;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.InstrumentType;
import ru.tinkoff.invest.openapi.model.rest.Orderbook;
import ru.tinkoff.invest.openapi.model.rest.PortfolioPosition;
import ru.tinkoff.invest.openapi.okhttp.OkHttpOpenApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Application {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String token = System.getenv("ssoToken");
        boolean isSandBoxMode = false;
        OpenApi api = new OkHttpOpenApi(token, isSandBoxMode);
        String BROKER_ACCOUNT_ID = api.getUserContext().getAccounts().get().getAccounts().get(0).getBrokerAccountId();
        String IIS_ACCOUNT_ID = api.getUserContext().getAccounts().get().getAccounts().get(1).getBrokerAccountId();

        System.out.println(IIS_ACCOUNT_ID);
        var contextBroker = api.getPortfolioContext()
                .getPortfolio(IIS_ACCOUNT_ID).get().getPositions();
        System.out.println(filterStocks(contextBroker));


//        System.out.println(api.getAuthToken());
//
//        String portfolio = String.valueOf(api.getUserContext().getAccounts().get().getAccounts().get(0));
//        String portfolio2 = String.valueOf(api.getUserContext().getAccounts().get().getAccounts().get(1));
//        System.out.println(portfolio);
//        System.out.println(portfolio2);
//
//        var contextBroker = api.getPortfolioContext().getPortfolio(BROKER_ACCOUNT_ID).get().getPositions();
//        var contextIis = api.getPortfolioContext().getPortfolio(IIS_ACCOUNT_ID).get().getPositions();
//
//        System.out.println(filterStocks(contextBroker));
//        System.out.println(filterStocks(contextIis));
    }

    public static StockPortfolioDto filterStocks(List<PortfolioPosition> portfolioPositionList) {
        var filterPortfolio = portfolioPositionList.stream()
                .filter(el -> el.getInstrumentType() == InstrumentType.BOND)
                .map(mi -> new StockPortfolio(
                        mi.getTicker(),
                        mi.getFigi(),
                        mi.getName(),
                        null,
                        mi.getAveragePositionPrice().getValue(),
                        mi.getBalance(),
                        mi.getInstrumentType().getValue(),
                        Currency.valueOf(mi.getExpectedYield().getCurrency().getValue()),
                        mi.getExpectedYield().getValue().doubleValue()
                ))
                .collect(Collectors.toList());

        return new StockPortfolioDto(filterPortfolio);
    }


}
