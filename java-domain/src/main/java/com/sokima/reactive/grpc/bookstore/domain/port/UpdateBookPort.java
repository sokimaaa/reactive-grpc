package com.sokima.reactive.grpc.bookstore.domain.port;

import com.sokima.reactive.grpc.bookstore.domain.Book;
import com.sokima.reactive.grpc.bookstore.domain.BookIdentity;
import com.sokima.reactive.grpc.bookstore.domain.Isbn;
import reactor.core.publisher.Mono;

public interface UpdateBookPort {

    <V> Mono<Container<BookIdentity>> updateBookIdentityField(final String checksum, final String field, final V value);

    Mono<Container<Book>> updateBookIsPurchasedField(final Isbn isbn, final Boolean isPurchased);

    record Container<DO>(
            DO oldDomainObject,
            DO newDomainObject,
            Boolean isUpdated
    ) {
    }
}
