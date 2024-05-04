package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update.transformer;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Proto2JavaTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update.option.UpdateOptionOneof;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.UpdateOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Request2UpdateOptionTransformer implements Proto2JavaTransformer<UpdateBookRequest, UpdateOption> {

    private static final Logger log = LoggerFactory.getLogger(Request2UpdateOptionTransformer.class);

    private final UpdateOptionOneof updateOptionOneof;

    public Request2UpdateOptionTransformer(final UpdateOptionOneof updateOptionOneof) {
        this.updateOptionOneof = updateOptionOneof;
    }

    @Override
    public UpdateOption transform(final UpdateBookRequest proto) {
        log.trace("Transforming to update option: {}", proto);
        return updateOptionOneof.resolve(proto);
    }
}
