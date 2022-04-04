package com.olegkand.tinkoffapi.controller;


import com.olegkand.tinkoffapi.dto.*;
import com.olegkand.tinkoffapi.model.Stock;
import com.olegkand.tinkoffapi.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.invest.openapi.model.rest.InstrumentType;
import ru.tinkoff.invest.openapi.model.rest.PortfolioPosition;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/stocks/{ticker}")
    public Stock getStock(@PathVariable String ticker){
        return stockService.getStocksByTicker(ticker);
    }

    @PostMapping("/stocks/getStocksByTickers")
    public StocksDto getStocksByTickers(@RequestBody TickersDto tickersDto){
        return stockService.getStocksByTickers(tickersDto);
    }

    @PostMapping("/stocks/getPrices")
    public StockPricesDto getPrices(@RequestBody FigiesDto figiesDto){
        return stockService.getStocksPrices(figiesDto);
    }

}
