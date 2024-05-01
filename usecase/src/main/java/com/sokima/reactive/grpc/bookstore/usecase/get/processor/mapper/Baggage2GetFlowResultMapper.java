package com.sokima.reactive.grpc.bookstore.usecase.get.processor.mapper;

import com.sokima.reactive.grpc.bookstore.domain.BookAggregation;
import com.sokima.reactive.grpc.bookstore.domain.helper.Baggage;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.ImmutableGetBookFlowResult;

public class Baggage2GetFlowResultMapper {
    private static final int MIN_AVAILABLE_QUANTITY = 0;

    public GetBookFlowResult mapToGetBookFlowResult(final Baggage<BookAggregation> baggage) {
        return ImmutableGetBookFlowResult.builder()
                .checksum(baggage.value().checksum())
                .author(baggage.bookIdentity().author())
                .title(baggage.bookIdentity().title())
                .edition(baggage.bookIdentity().edition())
                .description(baggage.bookIdentity().description())
                .isAvailable(baggage.value().quantity() > MIN_AVAILABLE_QUANTITY)
                .build();
    }
}
