package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update.option;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.ImmutableDescriptionUpdateOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.UpdateOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DescriptionUpdateOptionOneof extends AbstractOneofResolver<UpdateBookRequest, UpdateOption>
        implements UpdateOptionOneof {

    private static final Logger log = LoggerFactory.getLogger(DescriptionUpdateOptionOneof.class);

    @Override
    protected boolean condition(final UpdateBookRequest oneof) {
        log.trace("Passed description update option resolver with: {}", oneof);
        return oneof.hasUpdatedBookField() && oneof.getUpdatedBookField().hasDescription();
    }

    @Override
    protected UpdateOption result(final UpdateBookRequest oneof) {
        log.debug("Oneof is resolved as Description Update Option: {}", oneof);
        return ImmutableDescriptionUpdateOption.builder()
                .value(oneof.getUpdatedBookField().getDescription())
                .checksum(checksum(oneof))
                .build();
    }
}
