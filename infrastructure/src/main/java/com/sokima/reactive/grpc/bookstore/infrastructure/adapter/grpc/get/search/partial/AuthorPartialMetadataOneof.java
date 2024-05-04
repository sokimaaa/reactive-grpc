package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.search.partial;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.PartialBookMetadata;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.ImmutableAuthorSearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthorPartialMetadataOneof extends AbstractOneofResolver<PartialBookMetadata, SearchOption<String>>
        implements PartialMetadataOneof {

    private static final Logger log = LoggerFactory.getLogger(AuthorPartialMetadataOneof.class);

    @Override
    protected boolean condition(final PartialBookMetadata oneof) {
        log.trace("Passed author partial metadata option resolver with: {}", oneof);
        return oneof.hasAuthor();
    }

    @Override
    protected SearchOption<String> result(final PartialBookMetadata oneof) {
        log.debug("Oneof is resolved as Author Partial Metadata Option: {}", oneof);
        return ImmutableAuthorSearchOption.builder()
                .option(oneof.getAuthor())
                .build();
    }
}
