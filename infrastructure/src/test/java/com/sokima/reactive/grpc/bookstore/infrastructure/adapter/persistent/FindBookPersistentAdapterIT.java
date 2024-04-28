package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent;

import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
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
@Import(FindBookPersistentAdapter.class)
class FindBookPersistentAdapterIT {

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

    @Test
    void testAdapterAutowiring() {
        Assertions.assertNotNull(findBookPersistentAdapter);
    }

    @Test
    void testFindBookByChecksum() {
        final var checksum = "836838293aa332f46076b36eb87e9754";

        findBookPersistentAdapter.findBookByChecksum(checksum)
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualBookIdentity -> {
                    Assertions.assertEquals("To Kill a Mockingbird", actualBookIdentity.title());
                    Assertions.assertEquals("Harper Lee", actualBookIdentity.author());
                    Assertions.assertEquals("2nd Edition", actualBookIdentity.edition());
                    Assertions.assertEquals("A novel about the serious issues of rape and racial inequality.", actualBookIdentity.description());

                    Assertions.assertEquals(checksum, ChecksumGenerator.generateBookChecksum(actualBookIdentity));
                })
                .verifyComplete();
    }

    @Test
    void testFindBooksByTitle() {
        final var title = "1984";

        findBookPersistentAdapter.findBooksByTitle(title)
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualBookIdentity -> {
                    Assertions.assertEquals(title, actualBookIdentity.title());
                    Assertions.assertEquals("George Orwell", actualBookIdentity.author());
                    Assertions.assertEquals("Revised Edition", actualBookIdentity.edition());
                    Assertions.assertEquals("A dystopian novel set in a totalitarian regime.", actualBookIdentity.description());
                })
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void testFindBooksByAuthor() {
        final var author = "F. Scott Fitzgerald";

        findBookPersistentAdapter.findBooksByAuthor(author)
                .log()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void testFindBookAggregationByChecksum() {
        final var checksum = "836838293aa332f46076b36eb87e9754";

        findBookPersistentAdapter.findBookAggregationByChecksum(checksum)
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualBookAggregation -> {
                    Assertions.assertEquals(10L, actualBookAggregation.quantity());
                    Assertions.assertEquals(checksum, actualBookAggregation.checksum().trim());
                })
                .verifyComplete();
    }

    @Test
    void testNextBookByChecksumN() {
        final var checksum = "836838293aa332f46076b36eb87e9754";

        findBookPersistentAdapter.nextBookByChecksumN(checksum, 3L)
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualBook -> {
                    final var actualBookIdentity = actualBook.bookIdentity();
                    Assertions.assertEquals("To Kill a Mockingbird", actualBookIdentity.title());
                    Assertions.assertEquals("Harper Lee", actualBookIdentity.author());
                    Assertions.assertEquals("2nd Edition", actualBookIdentity.edition());

                    Assertions.assertNotNull(actualBook.isbn());
                })
                .expectNextCount(2)
                .verifyComplete();
    }
}
