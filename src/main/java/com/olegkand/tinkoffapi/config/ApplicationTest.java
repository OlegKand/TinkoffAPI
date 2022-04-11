package com.olegkand.tinkoffapi.config;

import com.olegkand.tinkoffapi.model.StockPortfolio;
import com.olegkand.tinkoffapi.dto.StockPortfolioDto;
import com.olegkand.tinkoffapi.model.Currency;
import ru.tinkoff.invest.openapi.model.rest.InstrumentType;
import ru.tinkoff.invest.openapi.model.rest.PortfolioPosition;
import ru.tinkoff.piapi.contract.v1.LastPrice;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ApplicationTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        InvestApi investApi = InvestApi.create(System.getenv("readToken"));
        List<Share> shareList = investApi.getInstrumentsService().getAllShares().join();
        Share yndx = shareList.stream().filter(el -> el.getTicker().equals("YNDX"))
                .findFirst()
                .orElseThrow();
        System.out.println(yndx);
//        List<String> figiList = new ArrayList<>();
//        figiList.add("BBG006L8G4H1");
//        LastPrice lastPriceYNDX = investApi.getMarketDataService().getLastPrices(figiList).join().stream().findFirst().orElseThrow();
//        System.out.println(lastPriceYNDX);
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
