package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.test;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import static java.lang.String.format;

@Testcontainers
public interface PostgresContainer {

    @Container
    PostgreSQLContainer<?> postgreSqlContainer = createPostgresContainer();

    @DynamicPropertySource
    static void configureProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.username", postgreSqlContainer::getUsername);
        registry.add("spring.r2dbc.password", postgreSqlContainer::getPassword);
        registry.add("spring.r2dbc.url", () ->
                format("r2dbc:postgresql://%s:%d/%s",
                        postgreSqlContainer.getHost(),
                        postgreSqlContainer.getFirstMappedPort(),
                        postgreSqlContainer.getDatabaseName())
        );
    }

    static PostgreSQLContainer<?> createPostgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.0"))
                .withDatabaseName("bookstore")
                .withUsername("postgres")
                .withPassword("postgres")
                .withCopyFileToContainer(
                        MountableFile.forClasspathResource("init.sql"),
                        "/docker-entrypoint-initdb.d/init.sql"
                );
    }
}
