package com.olegkand.tinkoffapi.exceptions;

public class StockNotFoundException extends RuntimeException{
    public StockNotFoundException(String massage) {
        super(massage);
    }
}
