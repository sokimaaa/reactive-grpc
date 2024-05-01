package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer;

import com.google.protobuf.Message;

/**
 * Java to Proto transformer.
 *
 * @param <P> the proto generated class.
 * @param <J> the pojo.
 */
public interface Java2ProtoTransformer<P extends Message, J> {
    P transform(final J pojo);
}
