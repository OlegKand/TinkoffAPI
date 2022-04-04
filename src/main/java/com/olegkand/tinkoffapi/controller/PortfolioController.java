package com.olegkand.tinkoffapi.controller;

import com.olegkand.tinkoffapi.dto.StockPortfolioDto;
import com.olegkand.tinkoffapi.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.invest.openapi.model.rest.InstrumentType;
import ru.tinkoff.invest.openapi.model.rest.PortfolioPosition;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping("/account/{checkNumber}")
    public String getAccountId(@PathVariable int checkNumber){
        return portfolioService.getAccount(checkNumber);
    }

    @GetMapping("/portfolio/getAllStocks/{accountId}")
    public StockPortfolioDto getAllPortfolio(@PathVariable String accountId){
        List<PortfolioPosition> portfolioPositions = portfolioService.getPortfolio(accountId);
        return portfolioService.getAllActive(portfolioPositions);
    }

    @GetMapping("/portfolio/getStocks/{accountId}")
    public StockPortfolioDto getStocksPortfolio(@PathVariable String accountId){
        List<PortfolioPosition> portfolioPositions = portfolioService.getPortfolio(accountId);
        return portfolioService.filterStocksByType(portfolioPositions, InstrumentType.STOCK);
    }

    @GetMapping("/portfolio/getBonds/{accountId}")
    public StockPortfolioDto getBondPortfolio(@PathVariable String accountId){
        List<PortfolioPosition> portfolioPositions = portfolioService.getPortfolio(accountId);
        return portfolioService.filterStocksByType(portfolioPositions, InstrumentType.BOND);
    }

    @GetMapping("/portfolio/getETF/{accountId}")
    public StockPortfolioDto getEtfPortfolio(@PathVariable String accountId){
        List<PortfolioPosition> portfolioPositions = portfolioService.getPortfolio(accountId);
        return portfolioService.filterStocksByType(portfolioPositions, InstrumentType.ETF);
    }
}
