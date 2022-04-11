package com.olegkand.tinkoffapi.controller;

import com.olegkand.tinkoffapi.dto.StockPortfolioDto;
import com.olegkand.tinkoffapi.model.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.invest.openapi.model.rest.InstrumentType;
import ru.tinkoff.invest.openapi.model.rest.PortfolioPosition;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PortfolioController {

}
