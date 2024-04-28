package com.sokima.reactive.grpc.bookstore.usecase.get.resolver.option;

import com.sokima.reactive.grpc.bookstore.domain.metadata.ImmutableFullBookMetadata;
import com.sokima.reactive.grpc.bookstore.domain.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.GetBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.ImmutableFullMetadataBookOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchBookOption;

public class FullMetadataSearchOptionOneofResolver extends AbstractOneofResolver<GetBookRequest, SearchBookOption<?>>
        implements SearchOptionOneofResolver {
    @Override
    public SearchBookOption<?> resolve(final GetBookRequest oneof) {
        if (!oneof.hasFullBookMetadata()) {
            return next().resolve(oneof);
        }
        final var fullBookMetadata = oneof.getFullBookMetadata();
        return ImmutableFullMetadataBookOption.builder()
                .option(
                        ImmutableFullBookMetadata.builder()
                                .title(fullBookMetadata.getTitle())
                                .author(fullBookMetadata.getAuthor())
                                .title(fullBookMetadata.getEdition())
                                .build()
                )
                .build();
    }
}
