package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.validator;

import com.google.protobuf.Message;

import java.util.function.UnaryOperator;

/**
 * Validates Message that received from outside or to be sent.
 *
 * @param <M> the proto generated message.
 */
@FunctionalInterface
public interface FieldValidator<M extends Message> extends UnaryOperator<M> {
    M validate(final M message);

    default M apply(final M message) {
        return validate(message);
    }
}
