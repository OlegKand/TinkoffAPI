package com.olegkand.tinkoffapi.dto;

import com.olegkand.tinkoffapi.model.StockPortfolio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Setter
@Getter
@AllArgsConstructor
@Value
public class StockPortfolioDto {
    private List<StockPortfolio> stockPortfolioList;

    AtomicInteger stockCounter = new AtomicInteger(1);

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (StockPortfolio stock : stockPortfolioList){
            sb.append("Stock number ").append(stockCounter).append(" :").append("\n");
            sb.append("    ticker: ").append(stock.getTicker()).append("\t");
            sb.append("    figi: ").append(stock.getFigi()).append("\t");
            sb.append("    name: ").append(stock.getName()).append("\t");
            sb.append("    lastPrice: ").append(stock.getLastPrice()).append(" ");
            sb.append(stock.getCurrency().toString()).append("\t");
            sb.append("    price: ").append(stock.getStartPrice()).append(" ");
            sb.append(stock.getCurrency().toString()).append("\t");
            sb.append("    lots: ").append(stock.getLots()).append(" ");
            sb.append("    VALUE: ").append(stock.getValue()).append("\n");
            stockCounter.incrementAndGet();
        }
        return sb.toString();
    }
}
