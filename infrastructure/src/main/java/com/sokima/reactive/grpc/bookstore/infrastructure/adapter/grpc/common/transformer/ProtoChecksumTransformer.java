package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer;

import com.sokima.reactive.grpc.bookstore.proto.Checksum;
import org.springframework.stereotype.Component;

@Component
public final class ProtoChecksumTransformer implements Java2ProtoTransformer<Checksum, String> {
    @Override
    public Checksum transform(final String checksum) {
        return Checksum.newBuilder()
                .setValue(checksum)
                .build();
    }
}
