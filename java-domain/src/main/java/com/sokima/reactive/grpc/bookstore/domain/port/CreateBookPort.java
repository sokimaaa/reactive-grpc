package com.sokima.reactive.grpc.bookstore.domain.port;

import com.sokima.reactive.grpc.bookstore.domain.Book;
import com.sokima.reactive.grpc.bookstore.domain.BookAggregation;
import com.sokima.reactive.grpc.bookstore.domain.BookIdentity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Port for interacting with third party component, e.g. database, services and et cetera.
 * Responsible for creating a new instance of Book, BookIdentity, or BookAggregation.
 */
public interface CreateBookPort {

    Mono<BookIdentity> createBookIdentity(final BookIdentity bookIdentity);

    Mono<BookAggregation> createEmptyBookAggregation(final String checksum);

    Flux<Book> createBookN(final BookIdentity identity, final Integer count);

    default Mono<Book> createBook(final BookIdentity identity) {
        return createBookN(identity, 1).single();
    }
}
