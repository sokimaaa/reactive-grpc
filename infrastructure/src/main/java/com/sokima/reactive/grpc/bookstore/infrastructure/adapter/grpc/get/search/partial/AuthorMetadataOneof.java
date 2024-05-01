package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.search.partial;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.PartialBookMetadata;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.ImmutableAuthorSearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchOption;
import org.springframework.stereotype.Component;

@Component
public class AuthorMetadataOneof extends AbstractOneofResolver<PartialBookMetadata, SearchOption<String>>
        implements PartialMetadataOneof {
    @Override
    protected boolean condition(final PartialBookMetadata oneof) {
        return oneof.hasAuthor();
    }

    @Override
    protected SearchOption<String> result(final PartialBookMetadata oneof) {
        return ImmutableAuthorSearchOption.builder()
                .option(oneof.getAuthor())
                .build();
    }
}
