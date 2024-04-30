package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent;

import com.sokima.reactive.grpc.bookstore.domain.Book;
import com.sokima.reactive.grpc.bookstore.domain.ImmutableIsbn;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.BookIdentityRepository;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.BookRepository;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.test.PersistentTestContext;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.test.PostgresContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

@DataR2dbcTest
@PersistentTestContext
@Import(UpdateBookPersistentAdapter.class)
class UpdateBookPersistentAdapterIT implements PostgresContainer {

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