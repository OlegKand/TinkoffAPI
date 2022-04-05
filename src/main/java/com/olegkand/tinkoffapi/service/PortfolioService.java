package com.olegkand.tinkoffapi.service;

import com.olegkand.tinkoffapi.dto.StockPortfolioDto;
import com.olegkand.tinkoffapi.model.Currency;
import ru.tinkoff.invest.openapi.model.rest.InstrumentType;
import ru.tinkoff.invest.openapi.model.rest.PortfolioPosition;

import java.util.List;
import java.util.Map;

public interface PortfolioService {

    List<PortfolioPosition> getPortfolio(String accountId);

    StockPortfolioDto getAllActive(List<PortfolioPosition> portfolioPositionList);

    StockPortfolioDto filterStocksByType(List<PortfolioPosition> portfolioPositionList, InstrumentType type);

    String getAccount(int checkNumber);

    Map<Currency, Double> getPortfolioValue(String accountId);
}
