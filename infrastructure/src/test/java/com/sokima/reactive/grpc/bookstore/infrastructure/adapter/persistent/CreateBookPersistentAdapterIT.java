package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent;

import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.BookIdentityRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Testcontainers
@Import({FindBookPersistentAdapter.class, CreateBookPersistentAdapter.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CreateBookPersistentAdapterIT {

    @Container
    static final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:14.0")
            .withDatabaseName("bookstore")
            .withUsername("postgres")
            .withPassword("postgres")
            .withCopyFileToContainer(MountableFile.forClasspathResource("init.sql"), "/docker-entrypoint-initdb.d/init.sql");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> String.format("r2dbc:postgresql://%s:%d/%s",
                postgresqlContainer.getHost(),
                postgresqlContainer.getFirstMappedPort(),
                postgresqlContainer.getDatabaseName()));
        registry.add("spring.r2dbc.username", postgresqlContainer::getUsername);
        registry.add("spring.r2dbc.password", postgresqlContainer::getPassword);
    }

    @Autowired(required = false)
    FindBookPersistentAdapter findBookPersistentAdapter;
    @Autowired(required = false)
    CreateBookPersistentAdapter createBookPersistentAdapter;

    @Test
    @Order(1)
    void testAdapterAutowiring() {
        Assertions.assertNotNull(createBookPersistentAdapter);
    }

    @Test
    @Order(2)
    void testCreateBookIdentity() {
        final var title = "1984";
        final var author = "George Orwell";
        final var edition = "3rd edition - test";

        findBookPersistentAdapter.findBookByMetadata(title, author, edition)
                .log()
                .as(StepVerifier::create)
                .verifyComplete();

        createBookPersistentAdapter.createBookIdentity(title, author, edition)
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualBookIdentity -> {
                    Assertions.assertEquals(title, actualBookIdentity.title());
                    Assertions.assertEquals(author, actualBookIdentity.author());
                    Assertions.assertEquals(edition, actualBookIdentity.edition());
                })
                .verifyComplete();

        findBookPersistentAdapter.findBookByMetadata(title, author, edition)
                .log()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @Order(3)
    void testCreateEmptyBookAggregation() {
        final var title = "1984";
        final var author = "George Orwell";
        final var edition = "3rd edition - test";
        final var checksum = ChecksumGenerator.generateBookChecksum(title, author, edition);

        findBookPersistentAdapter.findBookAggregationByChecksum(checksum)
                .log()
                .as(StepVerifier::create)
                .verifyComplete();

        createBookPersistentAdapter.createEmptyBookAggregation(checksum)
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualBookAggregation -> {
                    Assertions.assertEquals(checksum, actualBookAggregation.checksum());
                    Assertions.assertEquals(0L, actualBookAggregation.quantity());
                })
                .verifyComplete();

        findBookPersistentAdapter.findBookAggregationByChecksum(checksum)
                .log()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @Order(4)
    void testCreateBookN() {
        final var title = "1984";
        final var author = "George Orwell";
        final var edition = "3rd edition - test";
        final var checksum = ChecksumGenerator.generateBookChecksum(title, author, edition);

        findBookPersistentAdapter.findBookByChecksum(checksum)
                .log()
                .flatMapMany(bookIdentity ->
                        createBookPersistentAdapter.createBookN(bookIdentity, 3))
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualBook -> {
                    Assertions.assertNotNull(actualBook.isbn());

                    final var actualBookIdentity = actualBook.bookIdentity();
                    Assertions.assertEquals(title, actualBookIdentity.title());
                    Assertions.assertEquals(author, actualBookIdentity.author());
                    Assertions.assertEquals(edition, actualBookIdentity.edition());
                })
                .expectNextCount(2)
                .verifyComplete();

        findBookPersistentAdapter.nextBookByChecksumN(checksum, 3L)
                .log()
                .as(StepVerifier::create)
                .expectNextCount(3L)
                .verifyComplete();
    }
}