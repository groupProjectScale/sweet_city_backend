package com.example.configurations;

import com.example.configurations.properties.DatabaseProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@EnableConfigurationProperties({DatabaseProperties.class})
@Configuration
@Profile("!test")
public class DatabaseConfigurations {
    private final DatabaseProperties databaseProperties;

    public DatabaseConfigurations(DatabaseProperties databaseProperties) {
        this.databaseProperties = databaseProperties;
    }

    private HikariConfig getHikariConfig() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        hikariConfig.setUsername(databaseProperties.getUser());
        hikariConfig.setPassword(databaseProperties.getPassword());
        hikariConfig.setMinimumIdle(databaseProperties.getMinIdle());
        hikariConfig.setIdleTimeout(databaseProperties.getIdleTimeout());
        hikariConfig.setMaximumPoolSize(databaseProperties.getMaxPoolSize());
        hikariConfig.setConnectionTimeout(databaseProperties.getConnTimeout());
        hikariConfig.setPoolName(databaseProperties.getConnectionKey());
        hikariConfig.setJdbcUrl(databaseProperties.getJdbcUrl());
        return hikariConfig;
    }

    @Bean
    public DataSource dataSource() {
        return new HikariDataSource(getHikariConfig());
    }
}
