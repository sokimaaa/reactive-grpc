package com.sokima.reactive.grpc.bookstore.usecase.get.resolver.option.partial;

import com.sokima.reactive.grpc.bookstore.domain.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.PartialBookMetadata;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.ImmutableTitleBookOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchBookOption;

public class TitleMetadataOneofResolver extends AbstractOneofResolver<PartialBookMetadata, SearchBookOption<String>> implements PartialMetadataOneofResolver {
    @Override
    public SearchBookOption<String> resolve(final PartialBookMetadata oneof) {
        if (!oneof.hasTitle()) {
            return next().resolve(oneof);
        }
        return ImmutableTitleBookOption.builder()
                .option(oneof.getTitle())
                .build();
    }
}
