package com.sokima.reactive.grpc.bookstore.domain.port;

import com.sokima.reactive.grpc.bookstore.domain.Book;
import com.sokima.reactive.grpc.bookstore.domain.BookAggregation;
import com.sokima.reactive.grpc.bookstore.domain.BookIdentity;
import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FindBookPort {

    Mono<BookIdentity> findBookByChecksum(final String checksum);

    default Mono<BookIdentity> findBookByMetadata(final String title, final String author, final String edition) {
        return findBookByChecksum(ChecksumGenerator.generateBookChecksum(title, author, edition));
    }

    Flux<BookIdentity> findBooksByTitle(final String title);

    Flux<BookIdentity> findBooksByAuthor(final String author);

    Mono<BookAggregation> findBookAggregationByChecksum(final String checksum);

    Flux<Book> nextBookByChecksumN(final String checksum, final Long count);

    default Flux<Book> nextBookByIdentityN(final BookIdentity identity, final Long count) {
        final var checksum = ChecksumGenerator.generateBookChecksum(identity.title(), identity.author(), identity.edition());
        return nextBookByChecksumN(checksum, count);
    }

    default Mono<Book> nextBookByIdentity(final BookIdentity identity) {
        return nextBookByIdentityN(identity, 1L).single();
    }

    default Mono<Book> nextBookByChecksum(final String checksum) {
        return nextBookByChecksumN(checksum, 1L).single();
    }
}
