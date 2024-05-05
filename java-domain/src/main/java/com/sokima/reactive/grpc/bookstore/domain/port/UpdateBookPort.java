package com.sokima.reactive.grpc.bookstore.domain.port;

import com.sokima.reactive.grpc.bookstore.domain.Book;
import com.sokima.reactive.grpc.bookstore.domain.BookAggregation;
import com.sokima.reactive.grpc.bookstore.domain.BookIdentity;
import com.sokima.reactive.grpc.bookstore.domain.Isbn;
import reactor.core.publisher.Mono;

/**
 * Port for interacting with third party component, e.g. database, services and et cetera.
 * Responsible for updating existing instances of Book, BookIdentity, or BookAggregation.
 */
public interface UpdateBookPort {

    <V> Mono<Container<BookIdentity>> updateBookIdentityField(final String checksum, final String field, final V value);

    Mono<Container<Book>> updateBookIsPurchasedField(final Isbn isbn, final Boolean isPurchased);

    Mono<Container<BookAggregation>> updateBookAggregationQuantity(final String checksum, final Long quantity);

    record Container<DO>(
            DO oldDomainObject,
            DO newDomainObject,
            Boolean isUpdated
    ) {
    }
}
