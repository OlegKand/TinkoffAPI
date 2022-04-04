package com.olegkand.tinkoffapi.service;

import com.olegkand.tinkoffapi.dto.*;
import com.olegkand.tinkoffapi.model.Stock;
import ru.tinkoff.invest.openapi.model.rest.InstrumentType;
import ru.tinkoff.invest.openapi.model.rest.PortfolioPosition;

import java.util.List;

public interface StockService {

    Stock getStocksByTicker(String ticker);
    StocksDto getStocksByTickers(TickersDto tickersDto);
    StockPricesDto getStocksPrices(FigiesDto figiesDto);
}
