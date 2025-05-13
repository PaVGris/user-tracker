package com.diplom.analytics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.dialect.MySqlDialect;

@Configuration
public class ClickHouseDialectConfig {

    @Bean
    public Dialect clickHouseDialect() {
        return MySqlDialect.INSTANCE;
    }
}