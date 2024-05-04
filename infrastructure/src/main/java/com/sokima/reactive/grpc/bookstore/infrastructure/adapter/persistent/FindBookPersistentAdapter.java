package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent;

import com.sokima.reactive.grpc.bookstore.domain.Book;
import com.sokima.reactive.grpc.bookstore.domain.BookAggregation;
import com.sokima.reactive.grpc.bookstore.domain.BookIdentity;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
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
public class FindBookPersistentAdapter implements FindBookPort {

    private static final Logger log = LoggerFactory.getLogger(FindBookPersistentAdapter.class);

    private final BookIdentityRepository bookIdentityRepository;
    private final BookAggregationRepository bookAggregationRepository;
    private final BookRepository bookRepository;
    private final BookIdentityEntityTransformer bookIdentityTransformer;
    private final BookAggregationEntityTransformer bookAggregationTransformer;
    private final BookEntityTransformer bookTransformer;

    public FindBookPersistentAdapter(
            final BookIdentityRepository bookIdentityRepository,
            final BookAggregationRepository bookAggregationRepository,
            final BookRepository bookRepository,
            final BookIdentityEntityTransformer bookIdentityTransformer,
            final BookAggregationEntityTransformer bookAggregationTransformer,
            final BookEntityTransformer bookTransformer) {
        this.bookIdentityRepository = bookIdentityRepository;
        this.bookAggregationRepository = bookAggregationRepository;
        this.bookRepository = bookRepository;
        this.bookIdentityTransformer = bookIdentityTransformer;
        this.bookAggregationTransformer = bookAggregationTransformer;
        this.bookTransformer = bookTransformer;
    }

    @Override
    public Mono<BookIdentity> findBookByChecksum(final String checksum) {
        return bookIdentityRepository.findById(checksum)
                .map(bookIdentityTransformer::mapToBookIdentity)
                .doOnNext(bookIdentity -> log.trace("Found book identity: {}", bookIdentity));
    }

    @Override
    public Flux<BookIdentity> findBooksByTitle(final String title) {
        return bookIdentityRepository.findAllByTitle(title)
                .map(bookIdentityTransformer::mapToBookIdentity)
                .doOnNext(bookIdentity -> log.trace("Found book identity: {}", bookIdentity));
    }

    @Override
    public Flux<BookIdentity> findBooksByAuthor(final String author) {
        return bookIdentityRepository.findAllByAuthor(author)
                .map(bookIdentityTransformer::mapToBookIdentity)
                .doOnNext(bookIdentity -> log.trace("Found book identity: {}", bookIdentity));
    }

    @Override
    public Mono<BookAggregation> findBookAggregationByChecksum(final String checksum) {
        return bookAggregationRepository.findByChecksum(checksum)
                .map(bookAggregationTransformer::mapToBookAggregation)
                .doOnNext(bookAggregation -> log.trace("Found book aggregation: {}", bookAggregation));
    }

    @Override
    public Flux<Book> nextBookByChecksumN(final String checksum, final Long count) {
        return bookRepository.findAvailableBookWithLimit(checksum, count)
                .map(bookTransformer::mapToBook)
                .doOnNext(bookIdentity -> log.trace("Found book: {}", bookIdentity));
    }
}
