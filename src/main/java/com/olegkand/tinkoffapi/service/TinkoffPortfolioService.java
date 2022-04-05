package com.olegkand.tinkoffapi.service;

import com.olegkand.tinkoffapi.dto.StockPortfolioDto;
import com.olegkand.tinkoffapi.dto.StockPrice;
import com.olegkand.tinkoffapi.exceptions.StockNotFoundException;
import com.olegkand.tinkoffapi.model.Currency;
import com.olegkand.tinkoffapi.model.StockPortfolio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.InstrumentType;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrument;
import ru.tinkoff.invest.openapi.model.rest.Orderbook;
import ru.tinkoff.invest.openapi.model.rest.PortfolioPosition;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TinkoffPortfolioService implements PortfolioService{

    private final OpenApi openApi;

    @Override
    public List<PortfolioPosition> getPortfolio(String accountId) {
        try {
            return openApi.getPortfolioContext().getPortfolio(accountId).get().getPositions();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public StockPortfolioDto getAllActive(List<PortfolioPosition> portfolioPositionList) {
        var filterPortfolio = portfolioPositionList.stream()
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

        List<String> figies = new ArrayList<>();
        filterPortfolio.forEach(figi -> figies.add(figi.getFigi()));
        List<CompletableFuture<Optional<Orderbook>>> orderBooks = new ArrayList<>();
        figies.forEach(figi -> orderBooks.add(getOrderBookByFigi(figi)));

        var portfolio = orderBooks.stream()
                .map(CompletableFuture::join)
                .map(oo -> oo.orElseThrow(() -> new StockNotFoundException("Stock not found")))
                .map(orderbook -> new StockPrice(
                        orderbook.getFigi(),
                        orderbook.getLastPrice().doubleValue()
                )).collect(Collectors.toList());

        filterPortfolio.forEach(stock -> {
            portfolio.stream()
                    .filter(el -> el.getFigi().equals(stock.getFigi()))
                    .forEach(el -> {
                        if (el.getFigi().equals(stock.getFigi())){
                            stock.setLastPrice(el.getPrice());
                        }
                    });
        });

        return new StockPortfolioDto(filterPortfolio);
    }

    @Override
    public StockPortfolioDto filterStocksByType(List<PortfolioPosition> portfolioPositionList, InstrumentType type) {
        var filterPortfolio = portfolioPositionList.stream()
                .filter(el -> el.getInstrumentType() == type)
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

        List<String> figies = new ArrayList<>();
        filterPortfolio.forEach(figi -> figies.add(figi.getFigi()));
        List<CompletableFuture<Optional<Orderbook>>> orderBooks = new ArrayList<>();
        figies.forEach(figi -> orderBooks.add(getOrderBookByFigi(figi)));

        var portfolio = orderBooks.stream()
                .map(CompletableFuture::join)
                .map(oo -> oo.orElseThrow(() -> new StockNotFoundException("Stock not found")))
                .map(orderbook -> new StockPrice(
                        orderbook.getFigi(),
                        orderbook.getLastPrice().doubleValue()
                )).collect(Collectors.toList());

        filterPortfolio.forEach(stock -> {
            portfolio.stream()
                    .filter(el -> el.getFigi().equals(stock.getFigi()))
                    .forEach(el -> {
                        if (el.getFigi().equals(stock.getFigi())){
                            stock.setLastPrice(el.getPrice());
                        }
                    });
        });

        return new StockPortfolioDto(filterPortfolio);
    }

    @Override
    public String getAccount(int checkNumber) {
        try {
            return openApi.getUserContext().getAccounts().get().getAccounts().get(checkNumber).getBrokerAccountId();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Async
    public CompletableFuture<Optional<Orderbook>> getOrderBookByFigi(String figi){
        var orderBook = openApi.getMarketContext().getMarketOrderbook(figi, 0);
        log.info("Getting price {} from TINKOFF", figi);
        return orderBook;
    }

    @Override
    public Map<Currency, Double> getPortfolioValue(String accountId) {
        try {
            StockPortfolioDto allActive = getAllActive(openApi.getPortfolioContext()
                    .getPortfolio(accountId).get().getPositions());

            Map<Currency, Double> currencyDoubleMap = allActive.getStockPortfolioList().stream()
                    .collect(Collectors.groupingBy(StockPortfolio::getCurrency,
                            Collectors.summingDouble(StockPortfolio::getValue)));

            Double totalValue = 0.0;
            for (Map.Entry<Currency, Double> entry : currencyDoubleMap.entrySet()){
                if (entry.getKey().equals(Currency.USD)){
                    totalValue = totalValue + entry.getValue() * getUSDPrice(accountId);
                } else {
                    totalValue = totalValue + entry.getValue();
                }
            }

            currencyDoubleMap.put(Currency.TOTAL_RUB, totalValue);
            return currencyDoubleMap;

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Double getUSDPrice(String accountId) {
        StockPortfolioDto currency = filterStocksByType(getPortfolio(accountId), InstrumentType.CURRENCY);
        System.out.println(currency.getStockPortfolioList().get(0).getLastPrice());
        return currency.getStockPortfolioList().get(0).getLastPrice();
    }
}
