package com.olegkand.tinkoffapi.service;

import com.olegkand.tinkoffapi.dto.*;
import com.olegkand.tinkoffapi.exceptions.StockNotFoundException;
import com.olegkand.tinkoffapi.model.Currency;
import com.olegkand.tinkoffapi.model.Stock;
import com.olegkand.tinkoffapi.model.StockPortfolio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.model.rest.InstrumentType;
import ru.tinkoff.invest.openapi.model.rest.MarketInstrumentList;
import ru.tinkoff.invest.openapi.model.rest.Orderbook;
import ru.tinkoff.invest.openapi.model.rest.PortfolioPosition;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TinkoffStockService implements StockService{

    private final OpenApi openApi;

    @Async
    public CompletableFuture<MarketInstrumentList> getMarketInstrumentTicker(String ticker){
        var context = openApi.getMarketContext();
        return context.searchMarketInstrumentsByTicker(ticker);
    }

    @Override
    public Stock getStocksByTicker(String ticker) {
        var cf = getMarketInstrumentTicker(ticker);
        var list = cf.join().getInstruments();
        if (list.isEmpty()){
            throw new StockNotFoundException(String.format("Stock %s not found", ticker));
        }

        var item = list.get(0);
        return new Stock(
                item.getTicker(),
                item.getFigi(),
                item.getName(),
                item.getType().getValue(),
                Currency.valueOf(item.getCurrency().getValue()),
                "TINFOFF"
        );
    }

    @Override
    public StocksDto getStocksByTickers(TickersDto tickersDto) {
        List<CompletableFuture<MarketInstrumentList>> marketInstruments = new ArrayList<>();
        tickersDto.getTickers().forEach(ticker -> marketInstruments.add(getMarketInstrumentTicker(ticker)));
        List<Stock> stocks = marketInstruments.stream()
                .map(CompletableFuture::join)
                .map(mi -> {
                    if (!mi.getInstruments().isEmpty()){
                        return mi.getInstruments().get(0);
                    }
                    return null;
                })
                .filter(el -> Objects.nonNull(el))
                .map(mi -> new Stock(
                        mi.getTicker(),
                        mi.getFigi(),
                        mi.getName(),
                        mi.getType().getValue(),
                        Currency.valueOf(mi.getCurrency().getValue()),
                        "TINKOFF"
                ))
                .collect(Collectors.toList());

        return new StocksDto(stocks);
    }

    @Async
    public CompletableFuture<Optional<Orderbook>> getOrderBookByFigi(String figi){
        var orderBook = openApi.getMarketContext().getMarketOrderbook(figi, 0);
        log.info("Getting price {} from TINKOFF", figi);
        return orderBook;
    }


    @Override
    public StockPricesDto getStocksPrices(FigiesDto figiesDto) {
        long startTime = System.currentTimeMillis();

        List<CompletableFuture<Optional<Orderbook>>> orderBooks = new ArrayList<>();
        figiesDto.getFigies().forEach(figi -> orderBooks.add(getOrderBookByFigi(figi)));
        var listPrices = orderBooks.stream()
                .map(CompletableFuture::join)
                .map(oo -> oo.orElseThrow(() -> new StockNotFoundException("Stock not found")))
                .map(orderbook -> new StockPrice(
                        orderbook.getFigi(),
                        orderbook.getLastPrice().doubleValue()
                ))
                .collect(Collectors.toList());

        log.info("Time Async: {}", System.currentTimeMillis() - startTime);
        return new StockPricesDto(listPrices);
    }

}
