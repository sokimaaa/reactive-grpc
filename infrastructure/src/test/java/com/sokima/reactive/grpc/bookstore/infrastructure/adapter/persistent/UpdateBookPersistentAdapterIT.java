package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent;

import com.sokima.reactive.grpc.bookstore.domain.Book;
import com.sokima.reactive.grpc.bookstore.domain.BookIdentity;
import com.sokima.reactive.grpc.bookstore.domain.ImmutableIsbn;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.BookIdentityRepository;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
@Import(UpdateBookPersistentAdapter.class)
class UpdateBookPersistentAdapterIT {

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
    UpdateBookPersistentAdapter updateBookPersistentAdapter;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookIdentityRepository bookIdentityRepository;

    @Test
    void testAdapterAutowiring() {
        Assertions.assertNotNull(updateBookPersistentAdapter);
    }

    @Test
    void testUpdateBookIdentityField() {
        final var checksum = "c7ad44cbad762a5da0a452f9e8548d17";
        final var field = "description";
        final var value = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed.";

        bookIdentityRepository.findById(checksum)
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualBookEntity -> {
                    Assertions.assertNotEquals(value, actualBookEntity.getDescription());
                })
                .verifyComplete();

        updateBookPersistentAdapter.updateBookIdentityField(checksum, field, value)
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualBookIdentityContainer -> {
                    Assertions.assertTrue(actualBookIdentityContainer.isUpdated());

                    final var oldBookIdentity = actualBookIdentityContainer.oldDomainObject();
                    final var newBookIdentity = actualBookIdentityContainer.newDomainObject();

                    Assertions.assertEquals(oldBookIdentity.author(), newBookIdentity.author());
                    Assertions.assertEquals(oldBookIdentity.title(), newBookIdentity.title());
                    Assertions.assertEquals(oldBookIdentity.edition(), newBookIdentity.edition());

                    Assertions.assertNotEquals(oldBookIdentity.description(), newBookIdentity.description());
                    Assertions.assertEquals(value, newBookIdentity.description());
                })
                .verifyComplete();

        bookIdentityRepository.findById(checksum)
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualBookEntity -> {
                    Assertions.assertEquals(value, actualBookEntity.getDescription());
                })
                .verifyComplete();
    }

    @Test
    void testUpdateBookIsPurchasedField() {
        final var isbn = ImmutableIsbn.builder().isbn("9780141182638").build();

        bookRepository.findByIsbn(isbn.isbn())
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualBookEntity -> {
                    Assertions.assertFalse(actualBookEntity.getIsPurchased());
                })
                .verifyComplete();

        updateBookPersistentAdapter.updateBookIsPurchasedField(isbn, true)
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualBookContainer -> {
                    Assertions.assertTrue(actualBookContainer.isUpdated());

                    final Book oldBook = actualBookContainer.oldDomainObject();
                    final Book newBook = actualBookContainer.newDomainObject();
                    Assertions.assertEquals(oldBook.isbn().isbn(), newBook.isbn().isbn());

                    final var oldBookIdentity = oldBook.bookIdentity();
                    final var newBookIdentity = newBook.bookIdentity();

                    Assertions.assertEquals(oldBookIdentity.author(), newBookIdentity.author());
                    Assertions.assertEquals(oldBookIdentity.title(), newBookIdentity.title());
                    Assertions.assertEquals(oldBookIdentity.edition(), newBookIdentity.edition());
                })
                .verifyComplete();

        bookRepository.findByIsbn(isbn.isbn())
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualBookEntity -> {
                    Assertions.assertTrue(actualBookEntity.getIsPurchased());
                })
                .verifyComplete();
    }
}