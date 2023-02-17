package com.example.configurations.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Profile;

import javax.validation.constraints.NotEmpty;

@ConstructorBinding
@ConfigurationProperties("sweetcity.database")
@Profile("!test")
public class DatabaseProperties {
    @NotEmpty
    private final String connectionKey;
    @NotEmpty
    private final String connType;
    @NotEmpty
    private final String host;
    @NotEmpty
    private final String db;
    @NotEmpty
    private final String user;
    @NotEmpty
    private final String password;
    @NotEmpty
    private final Integer minIdle;
    @NotEmpty
    private final Integer idleTimeout;
    @NotEmpty
    private final Integer maxPoolSize;
    @NotEmpty
    private final Integer connTimeout;
    @NotEmpty
    private final Integer port;

    public DatabaseProperties(
            String connectionKey,
            String connType,
            String host,
            String db,
            String user,
            String password,
            Integer minIdle,
            Integer idleTimeout,
            Integer maxPoolSize,
            Integer connTimeout,
            Integer port
    ) {
        this.connectionKey = connectionKey;
        this.connType = connType;
        this.host = host;
        this.db = db;
        this.user = user;
        this.password = password;
        this.minIdle = minIdle;
        this.idleTimeout = idleTimeout;
        this.maxPoolSize = maxPoolSize;
        this.connTimeout = connTimeout;
        this.port = port;
    }

    public String getJdbcUrl() {
        return String.format(
                "jdbc:postgresql://%s:%s/%s?verifyServerCertificate=false&useSSL=false",
                getHost(),
                getPort(),
                getDb());
    }
    public String getConnectionKey() {
        return connectionKey;
    }

    public String getConnType() {
        return connType;
    }

    public String getHost() {
        return host;
    }

    public String getDb() {
        return db;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public Integer getIdleTimeout() {
        return idleTimeout;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public Integer getConnTimeout() {
        return connTimeout;
    }

    public Integer getPort() {
        return port;
    }
}

