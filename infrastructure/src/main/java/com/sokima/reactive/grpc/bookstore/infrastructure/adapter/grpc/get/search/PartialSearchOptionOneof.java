package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.search;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.search.partial.PartialMetadataOneof;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.GetBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PartialSearchOptionOneof extends AbstractOneofResolver<GetBookRequest, SearchOption<String>>
        implements SearchOptionOneof<String> {

    private static final Logger log = LoggerFactory.getLogger(PartialSearchOptionOneof.class);

    private final PartialMetadataOneof partialMetadataOneof;

    public PartialSearchOptionOneof(final PartialMetadataOneof partialMetadataOneof) {
        this.partialMetadataOneof = partialMetadataOneof;
    }

    @Override
    protected boolean condition(final GetBookRequest oneof) {
        log.trace("Passed partial search option resolver with: {}", oneof);
        return oneof.hasPartialBookMetadata();
    }

    @Override
    protected SearchOption<String> result(final GetBookRequest oneof) {
        log.debug("Oneof is resolved as Partial Search Option: {}", oneof);
        final var partialBookMetadata = oneof.getPartialBookMetadata();
        return partialMetadataOneof.resolve(partialBookMetadata);
    }
}
