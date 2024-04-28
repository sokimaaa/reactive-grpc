package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent;

import com.sokima.reactive.grpc.bookstore.domain.*;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.entity.BookAggregationEntity;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.entity.BookIdentityEntity;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.BookAggregationRepository;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.BookIdentityRepository;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.BookRepository;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.out.BookWithBookIdentityProjection;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class FindBookPersistentAdapter implements FindBookPort {

    private final BookIdentityRepository bookIdentityRepository;
    private final BookAggregationRepository bookAggregationRepository;
    private final BookRepository bookRepository;

    public FindBookPersistentAdapter(
            final BookIdentityRepository bookIdentityRepository,
            final BookAggregationRepository bookAggregationRepository,
            final BookRepository bookRepository) {
        this.bookIdentityRepository = bookIdentityRepository;
        this.bookAggregationRepository = bookAggregationRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Mono<BookIdentity> findBookByChecksum(final String checksum) {
        return bookIdentityRepository.findById(checksum)
                .map(this::mapToBookIdentity);
    }

    @Override
    public Flux<BookIdentity> findBooksByTitle(final String title) {
        return bookIdentityRepository.findAllByTitle(title)
                .map(this::mapToBookIdentity);
    }

    @Override
    public Flux<BookIdentity> findBooksByAuthor(final String author) {
        return bookIdentityRepository.findAllByAuthor(author)
                .map(this::mapToBookIdentity);
    }

    @Override
    public Mono<BookAggregation> findBookAggregationByChecksum(final String checksum) {
        return bookAggregationRepository.findByChecksum(checksum)
                .map(this::mapToBookAggregation);
    }

    @Override
    public Flux<Book> nextBookByChecksumN(final String checksum, final Long count) {
        return bookRepository.findAvailableBookWithLimit(checksum, count)
                .map(this::mapToBook);
    }

    private BookIdentity mapToBookIdentity(final BookIdentityEntity entity) {
        return ImmutableBookIdentity.builder()
                .author(entity.getAuthor())
                .title(entity.getTitle())
                .edition(entity.getEdition())
                .description(entity.getDescription())
                .build();
    }

    private BookAggregation mapToBookAggregation(final BookAggregationEntity entity) {
        return ImmutableBookAggregation.builder()
                .quantity(entity.getQuantity())
                .checksum(entity.getChecksum())
                .build();
    }

    private Book mapToBook(final BookWithBookIdentityProjection projection) {
        return ImmutableBook.builder()
                .isbn(
                        ImmutableIsbn.builder().isbn(projection.getIsbn()).build()
                )
                .bookIdentity(ImmutablePartialBookIdentity.builder()
                        .author(projection.getAuthor())
                        .edition(projection.getEdition())
                        .title(projection.getTitle())
                        .build())
                .build();
    }
}
