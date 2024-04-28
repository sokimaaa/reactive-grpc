package com.sokima.reactive.grpc.bookstore.usecase.get.resolver.option;

import com.sokima.reactive.grpc.bookstore.domain.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.GetBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.ImmutableChecksumBookOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchBookOption;

public class ChecksumSearchOptionOneofResolver extends AbstractOneofResolver<GetBookRequest, SearchBookOption<?>>
        implements SearchOptionOneofResolver {

    @Override
    public SearchBookOption<?> resolve(final GetBookRequest oneof) {
        if (!oneof.hasBookChecksum()) {
            return next().resolve(oneof);
        }
        return ImmutableChecksumBookOption.builder()
                .option(oneof.getBookChecksum().getValue())
                .build();
    }
}
