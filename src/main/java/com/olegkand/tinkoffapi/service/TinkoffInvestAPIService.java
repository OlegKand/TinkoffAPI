package com.olegkand.tinkoffapi.service;

import com.olegkand.tinkoffapi.model.Stock;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.core.InvestApi;

@Data
@RequiredArgsConstructor
@Slf4j
@Service
public class TinkoffInvestAPIService implements InvestApiService {

    private final InvestApi investApi = InvestApi.create(System.getenv("readToken"));

    @Override
    public String getPortfolioValue() {

        return null;
    }

    @Override
    public String getTopFive() {
        return null;
    }

    @Override
    public String getLowFive() {
        return null;
    }

    @Override
    public String getDividendsValue() {
        return null;
    }

    public String getShareByTicker(String ticker) {
        Share share = investApi.getInstrumentsService().getAllShares().join().stream()
                .filter(el -> el.getTicker().equalsIgnoreCase(ticker))
                .findFirst()
                .orElseThrow();
        String name = share.getName();
        return name;
    }
}
