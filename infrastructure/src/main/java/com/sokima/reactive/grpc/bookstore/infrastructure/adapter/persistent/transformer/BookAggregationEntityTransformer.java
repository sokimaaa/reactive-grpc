package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.transformer;

import com.sokima.reactive.grpc.bookstore.domain.BookAggregation;
import com.sokima.reactive.grpc.bookstore.domain.ImmutableBookAggregation;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.entity.BookAggregationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BookAggregationEntityTransformer {

    private static final Logger log = LoggerFactory.getLogger(BookAggregationEntityTransformer.class);

    public BookAggregation mapToBookAggregation(final BookAggregationEntity entity) {
        log.trace("Transforming to book aggregation: {}", entity);
        return ImmutableBookAggregation.builder()
                .quantity(entity.getQuantity())
                .checksum(entity.getChecksum())
                .build();
    }

    public BookAggregationEntity mapToBookAggregationEntity(final String checksum) {
        log.trace("Transforming to book aggregation entity: {}", checksum);
        final var bookAggregationEntity = new BookAggregationEntity();
        bookAggregationEntity.setChecksum(checksum);
        bookAggregationEntity.setQuantity(0L);
        return bookAggregationEntity;
    }

    public BookAggregationEntity enrichQuantity(final BookAggregationEntity entity, final Integer quantity) {
        log.trace("Transforming to book aggregation entity: {} with quantity: {}", entity, quantity);
        final var bookAggregationEntity = new BookAggregationEntity();
        bookAggregationEntity.setAggregationId(entity.getAggregationId());
        bookAggregationEntity.setQuantity(entity.getQuantity() + quantity);
        bookAggregationEntity.setChecksum(entity.getChecksum());
        return bookAggregationEntity;
    }
}
