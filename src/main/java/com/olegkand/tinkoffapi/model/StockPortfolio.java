package com.olegkand.tinkoffapi.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockPortfolio {
    String ticker;
    String figi;
    String name;
    BigDecimal lastPrice;
    BigDecimal startPrice;
    BigDecimal lots;
    String type;
    Currency currency;
    Double value;
}
