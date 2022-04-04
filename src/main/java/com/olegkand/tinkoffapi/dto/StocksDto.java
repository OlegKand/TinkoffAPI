package com.olegkand.tinkoffapi.dto;

import com.olegkand.tinkoffapi.model.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StocksDto {
     List<Stock> stockList;
}
