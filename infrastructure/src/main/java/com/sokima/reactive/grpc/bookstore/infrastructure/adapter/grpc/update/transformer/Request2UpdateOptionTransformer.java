package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update.transformer;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Proto2JavaTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update.option.UpdateOptionOneof;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.UpdateOption;
import org.springframework.stereotype.Component;

@Component
public class Request2UpdateOptionTransformer implements Proto2JavaTransformer<UpdateBookRequest, UpdateOption> {

    private final UpdateOptionOneof updateOptionOneof;

    public Request2UpdateOptionTransformer(final UpdateOptionOneof updateOptionOneof) {
        this.updateOptionOneof = updateOptionOneof;
    }

    @Override
    public UpdateOption transform(final UpdateBookRequest proto) {
        return updateOptionOneof.resolve(proto);
    }
}
