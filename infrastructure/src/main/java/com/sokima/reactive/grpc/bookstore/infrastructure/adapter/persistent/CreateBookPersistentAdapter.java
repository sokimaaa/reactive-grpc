package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent;

import com.sokima.reactive.grpc.bookstore.domain.Book;
import com.sokima.reactive.grpc.bookstore.domain.BookAggregation;
import com.sokima.reactive.grpc.bookstore.domain.BookIdentity;
import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.port.CreateBookPort;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.BookAggregationRepository;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.BookIdentityRepository;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.BookRepository;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.transformer.BookAggregationEntityTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.transformer.BookEntityTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.transformer.BookIdentityEntityTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class CreateBookPersistentAdapter implements CreateBookPort {

    private static final Logger log = LoggerFactory.getLogger(CreateBookPersistentAdapter.class);

    private final BookRepository bookRepository;
    private final BookIdentityRepository bookIdentityRepository;
    private final BookAggregationRepository bookAggregationRepository;
    private final BookIdentityEntityTransformer bookIdentityTransformer;
    private final BookAggregationEntityTransformer bookAggregationTransformer;
    private final BookEntityTransformer bookTransformer;

    public CreateBookPersistentAdapter(
            final BookRepository bookRepository,
            final BookIdentityRepository bookIdentityRepository,
            final BookAggregationRepository bookAggregationRepository,
            final BookIdentityEntityTransformer bookIdentityTransformer,
            final BookAggregationEntityTransformer bookAggregationTransformer,
            final BookEntityTransformer bookTransformer) {
        this.bookRepository = bookRepository;
        this.bookIdentityRepository = bookIdentityRepository;
        this.bookAggregationRepository = bookAggregationRepository;
        this.bookIdentityTransformer = bookIdentityTransformer;
        this.bookAggregationTransformer = bookAggregationTransformer;
        this.bookTransformer = bookTransformer;
    }

    @Override
    public Mono<BookIdentity> createBookIdentity(final BookIdentity bookIdentity) {
        final var bookIdentityEntity = bookIdentityTransformer.mapToBookIdentityEntity(bookIdentity);
        return Mono.just(bookIdentityEntity)
                .doOnNext(entity -> log.debug("Trying to create book identity based on: {}", entity))
                .flatMap(bookIdentityRepository::save)
                .map(bookIdentityTransformer::mapToPartialBookIdentity)
                .doOnNext(identity -> log.debug("Created book identity: {}", identity));
    }

    @Override
    public Mono<BookAggregation> createEmptyBookAggregation(final String checksum) {
        final var bookAggregationEntity = bookAggregationTransformer.mapToBookAggregationEntity(checksum);
        return Mono.just(bookAggregationEntity)
                .doOnNext(entity -> log.debug("Trying to create book aggregation based on: {}", entity))
                .flatMap(bookAggregationRepository::save)
                .map(bookAggregationTransformer::mapToBookAggregation)
                .doOnNext(aggregation -> log.debug("Created book aggregation: {}", aggregation));
    }

    @Override
    public Flux<Book> createBookN(final BookIdentity identity, final Long count) {
        return bookAggregationRepository.findByChecksum(ChecksumGenerator.generateBookChecksum(identity))
                .map(entity -> bookAggregationTransformer.incrementQuantity(entity, count))
                .flatMap(bookAggregationRepository::save)
                .map(bookAggregation -> bookTransformer.generateBookEntities(bookAggregation, count))
                .flatMapMany(bookRepository::saveAll)
                .map(bookEntity -> bookTransformer.mapToBook(bookEntity, identity))
                .doOnNext(book -> log.debug("Created book: {}", book));
    }
}
