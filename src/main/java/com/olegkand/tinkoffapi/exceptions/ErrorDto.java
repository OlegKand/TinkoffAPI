package com.olegkand.tinkoffapi.exceptions;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ErrorDto {
    private String error;
}
