package com.sokima.reactive.grpc.bookstore.usecase.get.processor;

import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookFlowResult;
import reactor.core.publisher.Flux;

public interface BookOptionProcessor<S extends SearchOption<?>> {
    Flux<GetBookFlowResult> process(final S searchBookOption);

    boolean support(final String type);
}
