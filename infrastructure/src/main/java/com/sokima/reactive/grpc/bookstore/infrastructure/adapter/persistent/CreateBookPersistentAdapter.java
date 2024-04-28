package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent;

import com.sokima.reactive.grpc.bookstore.domain.*;
import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.generator.IsbnGenerator;
import com.sokima.reactive.grpc.bookstore.domain.port.CreateBookPort;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.entity.BookAggregationEntity;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.entity.BookEntity;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.entity.BookIdentityEntity;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.BookAggregationRepository;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.BookIdentityRepository;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.BookRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

@Repository
public class CreateBookPersistentAdapter implements CreateBookPort {

    private final BookRepository bookRepository;
    private final BookIdentityRepository bookIdentityRepository;
    private final BookAggregationRepository bookAggregationRepository;

    public CreateBookPersistentAdapter(
            final BookRepository bookRepository,
            final BookIdentityRepository bookIdentityRepository,
            final BookAggregationRepository bookAggregationRepository) {
        this.bookRepository = bookRepository;
        this.bookIdentityRepository = bookIdentityRepository;
        this.bookAggregationRepository = bookAggregationRepository;
    }

    @Override
    public Mono<BookIdentity> createBookIdentity(
            final String title,
            final String author,
            final String edition) {
        final var bookIdentityEntity = new BookIdentityEntity();
        bookIdentityEntity.setIsNew(Boolean.TRUE);
        bookIdentityEntity.setChecksum(ChecksumGenerator.generateBookChecksum(title, author, edition));
        bookIdentityEntity.setAuthor(author);
        bookIdentityEntity.setTitle(title);
        bookIdentityEntity.setEdition(edition);
        return bookIdentityRepository.save(bookIdentityEntity)
                .map(this::mapToBookIdentity);
    }

    @Override
    public Mono<BookAggregation> createEmptyBookAggregation(
            final String checksum) {
        final var bookAggregationEntity = new BookAggregationEntity();
        bookAggregationEntity.setChecksum(checksum);
        bookAggregationEntity.setQuantity(0L);
        return bookAggregationRepository.save(bookAggregationEntity)
                .map(this::mapToBookAggregation);
    }

    @Override
    public Flux<Book> createBookN(
            final BookIdentity identity,
            final Integer count) {
        return bookAggregationRepository.findByChecksum(ChecksumGenerator.generateBookChecksum(identity))
                .flatMap(bookAggregation -> {
                    final var newQuantity = bookAggregation.getQuantity() + count;
                    final var bookAggregationEntity = copyOfWithQuantity(bookAggregation, newQuantity);
                    return bookAggregationRepository.save(bookAggregationEntity);
                })
                .flatMapMany(s -> {
                    final var bookEntities = Stream.generate(IsbnGenerator::generateIsbn)
                            .limit(count)
                            .map(isbn -> {
                                final var bookEntity = new BookEntity();
                                bookEntity.setIsbn(isbn);
                                bookEntity.setChecksum(ChecksumGenerator.generateBookChecksum(identity));
                                return bookEntity;
                            })
                            .toList();
                    return bookRepository.saveAll(bookEntities);
                })
                .map(bookEntity -> mapToBook(bookEntity, identity));
    }

    private BookAggregationEntity copyOfWithQuantity(final BookAggregationEntity entity, final Long quantity) {
        final var bookAggregationEntity = new BookAggregationEntity();
        bookAggregationEntity.setAggregationId(entity.getAggregationId());
        bookAggregationEntity.setQuantity(quantity);
        bookAggregationEntity.setChecksum(entity.getChecksum());
        return bookAggregationEntity;
    }

    private Book mapToBook(
            final BookEntity entity, final BookIdentity identity
    ) {
        return ImmutableBook.builder()
                .isbn(
                        ImmutableIsbn.builder()
                                .isbn(entity.getIsbn())
                                .build()
                )
                .bookIdentity(
                        identity
                )
                .build();
    }

    private BookAggregation mapToBookAggregation(
            final BookAggregationEntity entity
    ) {
        return ImmutableBookAggregation.builder()
                .checksum(entity.getChecksum())
                .quantity(entity.getQuantity())
                .build();
    }

    private BookIdentity mapToBookIdentity(
            final BookIdentityEntity entity
    ) {
        return ImmutablePartialBookIdentity.builder()
                .author(entity.getAuthor())
                .title(entity.getTitle())
                .edition(entity.getEdition())
                .build();
    }
}
