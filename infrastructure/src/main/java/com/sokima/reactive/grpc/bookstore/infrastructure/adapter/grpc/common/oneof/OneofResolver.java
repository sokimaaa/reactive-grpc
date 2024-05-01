package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof;

/**
 * Resolves oneof proto generated type to plain java.
 *
 * @param <INPUT>  the input proto message with oneof type.
 * @param <OUTPUT> the resolved pojo.
 */
public interface OneofResolver<INPUT, OUTPUT> {
    OUTPUT resolve(final INPUT oneof);
}
