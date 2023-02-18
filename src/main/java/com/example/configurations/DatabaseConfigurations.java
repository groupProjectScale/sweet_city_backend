package com.example.configurations;

import com.example.configurations.properties.DatabaseProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;
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
    public Jdbi jdbi() {
        Jdbi conn = Jdbi.create(new HikariDataSource(getHikariConfig()));
        conn.registerArrayType(String.class, "varchar");
        return conn;
    }
}
