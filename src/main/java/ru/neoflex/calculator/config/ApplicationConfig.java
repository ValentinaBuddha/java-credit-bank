package ru.neoflex.calculator.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.util.Objects;

@RequiredArgsConstructor
@Configuration
public class ApplicationConfig {

    private final Environment env;

    public BigDecimal getRate() {
        return new BigDecimal(Objects.requireNonNull(env.getProperty("rate")));
    }

    public BigDecimal getInsuranceRate() {
        return new BigDecimal(Objects.requireNonNull(env.getProperty("insurance")));
    }
}
