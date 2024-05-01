package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.search;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.search.partial.PartialMetadataOneof;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.GetBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchOption;
import org.springframework.stereotype.Component;

@Component
public class PartialSearchOptionOneof extends AbstractOneofResolver<GetBookRequest, SearchOption<String>>
        implements SearchOptionOneof<String> {
    private final PartialMetadataOneof partialMetadataOneof;

    public PartialSearchOptionOneof(final PartialMetadataOneof partialMetadataOneof) {
        this.partialMetadataOneof = partialMetadataOneof;
    }

    @Override
    protected boolean condition(final GetBookRequest oneof) {
        return oneof.hasPartialBookMetadata();
    }

    @Override
    protected SearchOption<String> result(final GetBookRequest oneof) {
        final var partialBookMetadata = oneof.getPartialBookMetadata();
        return partialMetadataOneof.resolve(partialBookMetadata);
    }
}
