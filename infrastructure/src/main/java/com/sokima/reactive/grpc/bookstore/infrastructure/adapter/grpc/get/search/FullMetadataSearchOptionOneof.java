package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.search;

import com.sokima.reactive.grpc.bookstore.domain.metadata.FullBookMetadata;
import com.sokima.reactive.grpc.bookstore.domain.metadata.ImmutableFullBookMetadata;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.GetBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.ImmutableFullMetadataSearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FullMetadataSearchOptionOneof extends AbstractOneofResolver<GetBookRequest, SearchOption<FullBookMetadata>>
        implements SearchOptionOneof<FullBookMetadata> {

    private static final Logger log = LoggerFactory.getLogger(FullMetadataSearchOptionOneof.class);

    @Override
    protected boolean condition(final GetBookRequest oneof) {
        log.trace("Passed full metadata search option resolver with: {}", oneof);
        return oneof.hasFullBookMetadata();
    }

    @Override
    protected SearchOption<FullBookMetadata> result(final GetBookRequest oneof) {
        log.debug("Oneof is resolved as Full Metadata Search Option: {}", oneof);
        final var fullBookMetadata = oneof.getFullBookMetadata();
        return ImmutableFullMetadataSearchOption.builder()
                .option(
                        ImmutableFullBookMetadata.builder()
                                .title(fullBookMetadata.getTitle())
                                .author(fullBookMetadata.getAuthor())
                                .edition(fullBookMetadata.getEdition())
                                .build()
                )
                .build();
    }
}
