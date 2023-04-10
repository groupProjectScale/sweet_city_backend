package com.example;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/** Base class for all test classes that require a PostgreSQL database container. */
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = ActivityServiceApplication.class)
@Testcontainers
public abstract class BaseTest {
    private static final Logger LOG = LoggerFactory.getLogger(BaseTest.class);
    private static final DockerImageName postgisImage =
            DockerImageName.parse("postgis/postgis:13-3.1-alpine")
                    .asCompatibleSubstituteFor("postgres");
    private static final String DATABASE_NAME = "sweetcity_test_postgresdb";
    private static final String DATABASE_USERNAME = "testuser";
    private static final String DATABASE_PASSWORD = "testpass";
    private static final String DATABASE_INIT_SCRIPT = "initdb.sql";
    private static Flyway flyway;

    @Container
    static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>(postgisImage)
                    .withDatabaseName(DATABASE_NAME)
                    .withUsername(DATABASE_USERNAME)
                    .withPassword(DATABASE_PASSWORD)
                    .withInitScript(DATABASE_INIT_SCRIPT);

    @BeforeAll
    static void setUp() {
        try {
            postgresContainer.start();
            flyway =
                    Flyway.configure()
                            .dataSource(
                                    postgresContainer.getJdbcUrl(),
                                    postgresContainer.getUsername(),
                                    postgresContainer.getPassword())
                            .locations("classpath:db/migration")
                            .load();
            flyway.migrate();
            LOG.info("PostgreSQL test container JDBC URL: {}", postgresContainer.getJdbcUrl());
        } catch (Exception e) {
            LOG.error("Failed to start PostgreSQL test container: {}", e.getMessage());
        }
    }

    @AfterAll
    static void tearDown() {
        try {
            postgresContainer.close();
        } catch (Exception e) {
            LOG.error("Failed to close PostgreSQL test container: {}", e.getMessage());
        }
    }

    @DynamicPropertySource
    static void setDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    protected DataSource getDataSource(Environment environment) {
        return DataSourceBuilder.create()
                .url(environment.getProperty("spring.datasource.url"))
                .username(environment.getProperty("spring.datasource.username"))
                .password(environment.getProperty("spring.datasource.password"))
                .build();
    }
}
