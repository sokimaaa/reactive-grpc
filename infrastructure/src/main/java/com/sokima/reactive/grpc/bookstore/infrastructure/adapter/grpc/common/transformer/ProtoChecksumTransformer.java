package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer;

import com.sokima.reactive.grpc.bookstore.proto.Checksum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public final class ProtoChecksumTransformer implements Java2ProtoTransformer<Checksum, String> {

    private static final Logger log = LoggerFactory.getLogger(ProtoChecksumTransformer.class);

    @Override
    public Checksum transform(final String checksum) {
        log.trace("Transforming to proto checksum: {}", checksum);
        return Checksum.newBuilder()
                .setValue(checksum)
                .build();
    }
}
