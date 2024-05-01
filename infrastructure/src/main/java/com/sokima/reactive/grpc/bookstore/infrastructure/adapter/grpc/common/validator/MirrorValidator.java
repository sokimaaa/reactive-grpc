package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.validator;

import com.google.protobuf.Message;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class MirrorValidator<M extends Message> implements FieldValidator<M> {
    @Override
    public M validate(final M message) {
        return message;
    }
}
