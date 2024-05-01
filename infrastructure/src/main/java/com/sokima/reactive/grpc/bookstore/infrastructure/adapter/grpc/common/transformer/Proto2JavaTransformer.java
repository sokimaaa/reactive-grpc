package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer;

import com.google.protobuf.Message;

/**
 * Proto to Java transformer.
 *
 * @param <P> the proto generated class.
 * @param <J> the pojo.
 */
public interface Proto2JavaTransformer<P extends Message, J> {
    J transform(final P proto);
}
