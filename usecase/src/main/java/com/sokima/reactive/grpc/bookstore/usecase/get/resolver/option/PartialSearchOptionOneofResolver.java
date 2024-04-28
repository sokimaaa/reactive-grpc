package com.sokima.reactive.grpc.bookstore.usecase.get.resolver.option;

import com.sokima.reactive.grpc.bookstore.domain.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.domain.oneof.OneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.GetBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.PartialBookMetadata;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchBookOption;

public class PartialSearchOptionOneofResolver extends AbstractOneofResolver<GetBookRequest, SearchBookOption<?>>
        implements SearchOptionOneofResolver {
    private final OneofResolver<PartialBookMetadata, SearchBookOption<String>> partialMetadataOneofResolver;

    public PartialSearchOptionOneofResolver(final OneofResolver<PartialBookMetadata, SearchBookOption<String>> partialMetadataOneofResolver) {
        this.partialMetadataOneofResolver = partialMetadataOneofResolver;
    }

    @Override
    public SearchBookOption<?> resolve(final GetBookRequest oneof) {
        if (!oneof.hasPartialBookMetadata()) {
            return next().resolve(oneof);
        }
        final var partialBookMetadata = oneof.getPartialBookMetadata();
        return partialMetadataOneofResolver.resolve(partialBookMetadata);
    }
}
