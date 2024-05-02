package com.sokima.reactive.grpc.bookstore.usecase.get.processor;

import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookFlowResult;
import reactor.core.publisher.Flux;

public class ErrorBookOptionProcessor<S extends SearchOption<?>> implements BookOptionProcessor<S> {
    @Override
    public Flux<GetBookFlowResult> process(final S searchBookOption) {
        throw new UnsupportedOperationException("Fallback processor. Unknown type of search option: " + searchBookOption.type());
    }

    @Override
    public boolean support(final String type) {
        return false;
    }
}
