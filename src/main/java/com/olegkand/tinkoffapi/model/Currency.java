package com.olegkand.tinkoffapi.model;

public enum Currency {
    RUB("RUB"),
    USD("USD"),
    EUR("EUR"),
    GBP("GBP"),
    HKD("HKD"),
    CHF("CHF"),
    JPY("JPY"),
    CNY("CNY"),
    TRY("TRY"),
    TOTAL_RUB("TOTAL_RUB");

    private String currency;

    Currency(String currency) {
        this.currency = currency;
    }
}
