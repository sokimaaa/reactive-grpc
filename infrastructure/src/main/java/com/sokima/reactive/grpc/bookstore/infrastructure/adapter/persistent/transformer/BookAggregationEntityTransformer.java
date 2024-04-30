package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.transformer;

import com.sokima.reactive.grpc.bookstore.domain.BookAggregation;
import com.sokima.reactive.grpc.bookstore.domain.ImmutableBookAggregation;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.entity.BookAggregationEntity;
import org.springframework.stereotype.Component;

@Component
public class BookAggregationEntityTransformer {
    public BookAggregation mapToBookAggregation(final BookAggregationEntity entity) {
        return ImmutableBookAggregation.builder()
                .quantity(entity.getQuantity())
                .checksum(entity.getChecksum())
                .build();
    }

    public BookAggregationEntity mapToBookAggregationEntity(final String checksum) {
        final var bookAggregationEntity = new BookAggregationEntity();
        bookAggregationEntity.setChecksum(checksum);
        bookAggregationEntity.setQuantity(0L);
        return bookAggregationEntity;
    }

    public BookAggregationEntity enrichQuantity(final BookAggregationEntity entity, final Integer quantity) {
        final var bookAggregationEntity = new BookAggregationEntity();
        bookAggregationEntity.setAggregationId(entity.getAggregationId());
        bookAggregationEntity.setQuantity(entity.getQuantity() + quantity);
        bookAggregationEntity.setChecksum(entity.getChecksum());
        return bookAggregationEntity;
    }
}
