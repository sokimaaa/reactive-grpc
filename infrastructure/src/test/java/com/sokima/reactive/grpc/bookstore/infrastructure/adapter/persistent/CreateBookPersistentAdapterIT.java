package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent;

import com.sokima.reactive.grpc.bookstore.domain.ImmutablePartialBookIdentity;
import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.BookAggregationRepository;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.BookIdentityRepository;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.test.PersistentTestContext;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.test.PostgresContainer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import({FindBookPersistentAdapter.class, CreateBookPersistentAdapter.class})
@PersistentTestContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CreateBookPersistentAdapterIT implements PostgresContainer {
    @Autowired(required = false)
    FindBookPersistentAdapter findBookPersistentAdapter;
    @Autowired(required = false)
    CreateBookPersistentAdapter createBookPersistentAdapter;
    @Autowired
    BookAggregationRepository bookAggregationRepository;

    @Autowired
    BookIdentityRepository bookIdentityRepository;

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
        final var checksum = ChecksumGenerator.generateBookChecksum(title, author, edition);
        final var bookIdentity = ImmutablePartialBookIdentity.builder()
                .title(title)
                .author(author)
                .edition(edition)
                .build();

        bookIdentityRepository.findById(checksum)
                .log()
                .as(StepVerifier::create)
                .verifyComplete();

        createBookPersistentAdapter.createBookIdentity(bookIdentity)
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualBookIdentity -> {
                    Assertions.assertEquals(title, actualBookIdentity.title());
                    Assertions.assertEquals(author, actualBookIdentity.author());
                    Assertions.assertEquals(edition, actualBookIdentity.edition());
                })
                .verifyComplete();

        bookIdentityRepository.findById(checksum)
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

        bookAggregationRepository.findByChecksum(checksum)
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

        bookAggregationRepository.findByChecksum(checksum)
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

        bookAggregationRepository.findByChecksum(checksum)
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(bookAggregation -> Assertions.assertEquals(3L, bookAggregation.getQuantity()))
                .verifyComplete();
    }
}