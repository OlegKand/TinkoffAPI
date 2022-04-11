package com.olegkand.tinkoffapi.config;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.okhttp.OkHttpOpenApi;
import ru.tinkoff.piapi.core.InvestApi;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {


}
