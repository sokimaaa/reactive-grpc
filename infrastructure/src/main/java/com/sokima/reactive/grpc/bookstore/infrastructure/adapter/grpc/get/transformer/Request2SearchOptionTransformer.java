package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.transformer;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Proto2JavaTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.search.SearchOptionOneof;
import com.sokima.reactive.grpc.bookstore.proto.GetBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Request2SearchOptionTransformer implements Proto2JavaTransformer<GetBookRequest, SearchOption<?>> {

    private static final Logger log = LoggerFactory.getLogger(Request2SearchOptionTransformer.class);

    private final SearchOptionOneof<?> oneofResolver;

    public Request2SearchOptionTransformer(final SearchOptionOneof<?> oneofResolver) {
        this.oneofResolver = oneofResolver;
    }

    @Override
    public SearchOption<?> transform(final GetBookRequest proto) {
        log.trace("Transforming to search option: {}", proto);
        return oneofResolver.resolve(proto);
    }
}
