package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.entity.BookIdentityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BookIdentityRepository extends ReactiveCrudRepository<BookIdentityEntity, String> {

    Flux<BookIdentityEntity> findAllByTitle(final String title);

    Flux<BookIdentityEntity> findAllByAuthor(final String author);
}
