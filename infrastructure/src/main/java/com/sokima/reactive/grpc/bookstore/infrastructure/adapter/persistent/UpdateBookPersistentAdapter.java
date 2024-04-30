package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent;

import com.sokima.reactive.grpc.bookstore.domain.Book;
import com.sokima.reactive.grpc.bookstore.domain.BookIdentity;
import com.sokima.reactive.grpc.bookstore.domain.Isbn;
import com.sokima.reactive.grpc.bookstore.domain.port.UpdateBookPort;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.entity.BookEntity;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.entity.BookIdentityEntity;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.BookIdentityRepository;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.BookRepository;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.transformer.BookEntityTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.transformer.BookIdentityEntityTransformer;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import static java.lang.String.format;

@Repository
public class UpdateBookPersistentAdapter implements UpdateBookPort {

    private static final String SQL_DYNAMIC_UPDATE_FIELD = "UPDATE book_identity SET %s = :value WHERE checksum = :checksum RETURNING *";

    private final BookRepository bookRepository;
    private final BookIdentityRepository bookIdentityRepository;
    private final DatabaseClient databaseClient;
    private final BookIdentityEntityTransformer bookIdentityTransformer;
    private final BookEntityTransformer bookTransformer;

    public UpdateBookPersistentAdapter(
            final BookRepository bookRepository,
            final BookIdentityRepository bookIdentityRepository,
            final DatabaseClient databaseClient,
            final BookIdentityEntityTransformer bookIdentityTransformer,
            final BookEntityTransformer bookTransformer) {
        this.bookRepository = bookRepository;
        this.bookIdentityRepository = bookIdentityRepository;
        this.databaseClient = databaseClient;
        this.bookIdentityTransformer = bookIdentityTransformer;
        this.bookTransformer = bookTransformer;
    }

    @Override
    public <V> Mono<Container<BookIdentity>> updateBookIdentityField(
            final String checksum, final String field, final V value) {
        return bookIdentityRepository.findById(checksum)
                .flatMap(bookIdentity -> databaseClient.sql(format(SQL_DYNAMIC_UPDATE_FIELD, field))
                        .bind("value", value)
                        .bind("checksum", checksum)
                        .fetch()
                        .first()
                        .map(bookIdentityTransformer::mapRow)
                        .map(entity -> composeContainer(bookIdentity, entity))
                )
                .map(this::recomposeContainer)
                .switchIfEmpty(
                        bookIdentityRepository.findById(checksum)
                                .map(bookIdentityTransformer::mapToBookIdentity)
                                .map(this::failUpdateContainer)
                );
    }


    @Override
    public Mono<Container<Book>> updateBookIsPurchasedField(
            final Isbn isbn, final Boolean isPurchased) {
        return bookRepository.findByIsbn(isbn.isbn())
                .map(bookTransformer::copyBookWithPurchased)
                .flatMap(bookRepository::save)
                .map(entity -> composeContainer(entity, entity))
                .flatMap(container -> Mono.zip(
                                        bookIdentityRepository.findById(container.oldDomainObject().getChecksum()),
                                        bookIdentityRepository.findById(container.newDomainObject().getChecksum())
                                )
                                .mapNotNull(tuple2BookIdentity -> enrichContainer(container, tuple2BookIdentity))
                );
    }

    private Container<Book> enrichContainer(
            final Container<BookEntity> bookEntityContainer,
            final Tuple2<BookIdentityEntity, BookIdentityEntity> tuple2BookIdentity) {
        return new Container<>(
                bookTransformer.mapToBook(bookEntityContainer.oldDomainObject(), tuple2BookIdentity.getT1()),
                bookTransformer.mapToBook(bookEntityContainer.newDomainObject(), tuple2BookIdentity.getT2()),
                bookEntityContainer.isUpdated()
        );
    }

    private Container<BookIdentity> recomposeContainer(final Container<BookIdentityEntity> container) {
        return new Container<>(
                bookIdentityTransformer.mapToBookIdentity(container.oldDomainObject()),
                bookIdentityTransformer.mapToBookIdentity(container.newDomainObject()),
                container.isUpdated()
        );
    }

    private <V> Container<V> composeContainer(final V oldVersion, final V newVersion) {
        return new Container<>(oldVersion, newVersion, Boolean.TRUE);
    }

    private <V> Container<V> failUpdateContainer(final V oldDomainObject) {
        return new Container<>(oldDomainObject, null, Boolean.FALSE);
    }
}
