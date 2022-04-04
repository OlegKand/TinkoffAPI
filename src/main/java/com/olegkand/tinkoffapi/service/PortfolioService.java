package com.olegkand.tinkoffapi.service;

import com.olegkand.tinkoffapi.dto.StockPortfolioDto;
import ru.tinkoff.invest.openapi.model.rest.InstrumentType;
import ru.tinkoff.invest.openapi.model.rest.PortfolioPosition;

import java.util.List;

public interface PortfolioService {

    List<PortfolioPosition> getPortfolio(String accountId);

    StockPortfolioDto getAllActive(List<PortfolioPosition> portfolioPositionList);

    StockPortfolioDto filterStocksByType(List<PortfolioPosition> portfolioPositionList, InstrumentType type);

    String getAccount(int checkNumber);
}
