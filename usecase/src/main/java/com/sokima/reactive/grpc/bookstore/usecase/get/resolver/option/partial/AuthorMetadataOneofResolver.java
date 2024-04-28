package com.sokima.reactive.grpc.bookstore.usecase.get.resolver.option.partial;

import com.sokima.reactive.grpc.bookstore.domain.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.domain.oneof.OneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.PartialBookMetadata;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.ImmutableAuthorBookOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchBookOption;

public class AuthorMetadataOneofResolver extends AbstractOneofResolver<PartialBookMetadata, SearchBookOption<String>> implements PartialMetadataOneofResolver {
    @Override
    public SearchBookOption<String> resolve(final PartialBookMetadata oneof) {
        if (!oneof.hasAuthor()) {
            return next().resolve(oneof);
        }
        return ImmutableAuthorBookOption.builder()
                .option(oneof.getAuthor())
                .build();
    }
}
