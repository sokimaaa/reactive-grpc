package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer;

import com.sokima.reactive.grpc.bookstore.proto.ISBN;
import org.springframework.stereotype.Component;

@Component
public final class ProtoIsbnTransformer implements Java2ProtoTransformer<ISBN, String>{
    @Override
    public ISBN transform(final String pojo) {
        return ISBN.newBuilder()
                .setValue(pojo)
                .build();
    }
}
