package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.entity.BookAggregationEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface BookAggregationRepository extends ReactiveCrudRepository<BookAggregationEntity, Long> {

    Mono<BookAggregationEntity> findByChecksum(final String checksum);
}
