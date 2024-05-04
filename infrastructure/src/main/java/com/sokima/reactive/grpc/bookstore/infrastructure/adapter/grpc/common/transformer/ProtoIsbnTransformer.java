package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer;

import com.sokima.reactive.grpc.bookstore.proto.ISBN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public final class ProtoIsbnTransformer implements Java2ProtoTransformer<ISBN, String> {

    private static final Logger log = LoggerFactory.getLogger(ProtoIsbnTransformer.class);

    @Override
    public ISBN transform(final String isbn) {
        log.trace("Transforming to proto isbn: {}", isbn);
        return ISBN.newBuilder()
                .setValue(isbn)
                .build();
    }
}
