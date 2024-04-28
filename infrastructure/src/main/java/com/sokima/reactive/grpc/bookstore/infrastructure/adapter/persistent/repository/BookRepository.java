package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.entity.BookEntity;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.out.BookWithBookIdentityProjection;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookRepository extends ReactiveCrudRepository<BookEntity, String> {

    Mono<BookEntity> findByIsbn(final String isbn);

    @Query("SELECT b.isbn, bi.title, bi.author, bi.edition FROM book b INNER JOIN book_identity bi ON b.checksum = bi.checksum WHERE NOT b.is_purchased AND b.checksum = :checksum LIMIT :limit")
    Flux<BookWithBookIdentityProjection> findAvailableBookWithLimit(final String checksum, final Long limit);
}
