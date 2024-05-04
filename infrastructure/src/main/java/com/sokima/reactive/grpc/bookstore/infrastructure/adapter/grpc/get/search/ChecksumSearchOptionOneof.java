package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.search;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.GetBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.ImmutableChecksumSearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ChecksumSearchOptionOneof extends AbstractOneofResolver<GetBookRequest, SearchOption<String>>
        implements SearchOptionOneof<String> {

    private static final Logger log = LoggerFactory.getLogger(ChecksumSearchOptionOneof.class);

    @Override
    protected boolean condition(final GetBookRequest oneof) {
        log.trace("Passed checksum search option resolver with: {}", oneof);
        return oneof.hasBookChecksum();
    }

    @Override
    protected SearchOption<String> result(final GetBookRequest oneof) {
        log.debug("Oneof is resolved as Checksum Search Option: {}", oneof);
        return ImmutableChecksumSearchOption.builder()
                .option(oneof.getBookChecksum().getValue())
                .build();
    }
}
