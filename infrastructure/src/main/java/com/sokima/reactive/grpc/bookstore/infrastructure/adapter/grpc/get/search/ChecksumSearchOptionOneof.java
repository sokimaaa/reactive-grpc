package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.search;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.GetBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.ImmutableChecksumSearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchOption;
import org.springframework.stereotype.Component;

@Component
public class ChecksumSearchOptionOneof extends AbstractOneofResolver<GetBookRequest, SearchOption<String>>
        implements SearchOptionOneof<String> {
    @Override
    protected boolean condition(final GetBookRequest oneof) {
        return oneof.hasBookChecksum();
    }

    @Override
    protected SearchOption<String> result(final GetBookRequest oneof) {
        return ImmutableChecksumSearchOption.builder()
                .option(oneof.getBookChecksum().getValue())
                .build();
    }
}
