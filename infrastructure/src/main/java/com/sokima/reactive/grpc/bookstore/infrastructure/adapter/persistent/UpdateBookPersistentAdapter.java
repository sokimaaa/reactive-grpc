package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent;

import com.sokima.reactive.grpc.bookstore.domain.*;
import com.sokima.reactive.grpc.bookstore.domain.port.UpdateBookPort;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.entity.BookEntity;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.entity.BookIdentityEntity;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.BookIdentityRepository;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.BookRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Map;

import static java.lang.String.format;

@Repository
public class UpdateBookPersistentAdapter implements UpdateBookPort {

    private static final String SQL_DYNAMIC_UPDATE_FIELD = "UPDATE book_identity SET %s = :value WHERE checksum = :checksum RETURNING *";

    private final BookRepository bookRepository;
    private final BookIdentityRepository bookIdentityRepository;
    private final DatabaseClient databaseClient;

    public UpdateBookPersistentAdapter(
            final BookRepository bookRepository,
            final BookIdentityRepository bookIdentityRepository,
            final DatabaseClient databaseClient) {
        this.bookRepository = bookRepository;
        this.bookIdentityRepository = bookIdentityRepository;
        this.databaseClient = databaseClient;
    }

    @Override
    public <V> Mono<Container<BookIdentity>> updateBookIdentityField(
            final String checksum, final String field, final V value) {
        return bookIdentityRepository.findById(checksum)
                .flatMap(bookIdentity -> {
                    return databaseClient.sql(format(SQL_DYNAMIC_UPDATE_FIELD, field))
                            .bind("value", value)
                            .bind("checksum", checksum)
                            .fetch()
                            .first()
                            .map(this::rowMapper)
                            .map(entity -> new Container<>(bookIdentity, entity, Boolean.TRUE));
                })
                .map(container -> new Container<>(
                        mapToBookIdentity(container.oldDomainObject()),
                        mapToBookIdentity(container.newDomainObject()),
                        container.isUpdated()
                ));
    }

    @Override
    public Mono<Container<Book>> updateBookIsPurchasedField(
            final Isbn isbn, final Boolean isPurchased) {
        return bookRepository.findByIsbn(isbn.isbn())
                .flatMap(oldEntity -> {
                    final BookEntity newEntity = copyWithIsPurchased(oldEntity);
                    return bookRepository.save(newEntity)
                            .map(entity -> new Container<>(oldEntity, entity, Boolean.TRUE))
                            .flatMap(container -> Mono.zip(
                                            bookIdentityRepository.findById(container.oldDomainObject().getChecksum()),
                                            bookIdentityRepository.findById(container.newDomainObject().getChecksum())
                                    )
                                    .mapNotNull(tuple2BookIdentity -> enrichContainer(container, tuple2BookIdentity)));
                });
    }

    private BookIdentity mapToBookIdentity(final BookIdentityEntity entity) {
        return ImmutableBookIdentity.builder()
                .title(entity.getTitle())
                .author(entity.getAuthor())
                .edition(entity.getEdition())
                .description(entity.getDescription())
                .build();
    }

    private BookEntity copyWithIsPurchased(final BookEntity entity) {
        final var bookEntity = new BookEntity();
        bookEntity.setBookId(entity.getBookId());
        bookEntity.setIsbn(entity.getIsbn());
        bookEntity.setChecksum(entity.getChecksum());
        bookEntity.setIsPurchased(Boolean.TRUE);
        return bookEntity;
    }

    private Book mapToBook(final BookEntity bookEntity, final BookIdentityEntity bookIdentityEntity) {
        return ImmutableBook.builder()
                .isbn(ImmutableIsbn.builder().isbn(bookEntity.getIsbn()).build())
                .bookIdentity(
                        ImmutablePartialBookIdentity.builder()
                                .title(bookIdentityEntity.getTitle())
                                .author(bookIdentityEntity.getAuthor())
                                .edition(bookIdentityEntity.getEdition())
                                .build()
                )
                .build();
    }

    private Container<Book> enrichContainer(
            final Container<BookEntity> bookEntityContainer,
            final Tuple2<BookIdentityEntity, BookIdentityEntity> tuple2BookIdentity) {
        return new Container<>(
                mapToBook(bookEntityContainer.oldDomainObject(), tuple2BookIdentity.getT1()),
                mapToBook(bookEntityContainer.newDomainObject(), tuple2BookIdentity.getT2()),
                bookEntityContainer.isUpdated()
        );
    }

    private BookIdentityEntity rowMapper(final Map<String, Object> row) {
        final var entity = new BookIdentityEntity();
        entity.setChecksum((String) row.get("checksum"));
        entity.setAuthor((String) row.get("author"));
        entity.setTitle((String) row.get("title"));
        entity.setEdition((String) row.get("edition"));
        entity.setDescription((String) row.get("description"));
        return entity;
    }
}
