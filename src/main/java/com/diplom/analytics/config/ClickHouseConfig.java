package com.diplom.analytics.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableJdbcRepositories(
        basePackages = "com.diplom.analytics.tool.repository",
        jdbcOperationsRef = "clickHouseJdbcOperations",
        transactionManagerRef = "clickHouseTransactionManager"
)
public class ClickHouseConfig {

    @Value("${spring.datasource.clickhouse.driver-class-name}")
    private String driver;

    @Value("${spring.datasource.clickhouse.url}")
    private String url;

    @Value("${spring.datasource.clickhouse.username}")
    private String username;

    @Value("${spring.datasource.clickhouse.password}")
    private String password;

    @Bean(name = "clickHouseDataSource")
    public DataSource clickHouseDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(driver, "ClickHouse driver must be specified"));
        dataSource.setUrl(Objects.requireNonNull(url, "ClickHouse URL must be specified"));
        dataSource.setUsername(Objects.requireNonNull(username, "ClickHouse username must be specified"));
        dataSource.setPassword(Objects.requireNonNull(password, "ClickHouse password must be specified"));
        return dataSource;
    }

    @Bean(name = "clickHouseJdbcTemplate")
    public NamedParameterJdbcTemplate clickHouseJdbcTemplate(DataSource clickHouseDataSource) {
        return new NamedParameterJdbcTemplate(clickHouseDataSource);
    }

    @Bean(name = "clickHouseTransactionManager")
    public PlatformTransactionManager clickHouseTransactionManager(
            @Qualifier("clickHouseDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public DataSourceTransactionManager toolTransactionManager(
            @Qualifier("clickHouseDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}