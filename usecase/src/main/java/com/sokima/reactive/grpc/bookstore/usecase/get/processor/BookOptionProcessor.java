package com.sokima.reactive.grpc.bookstore.usecase.get.processor;

import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchBookOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookResponse;
import reactor.core.publisher.Flux;

public interface BookOptionProcessor<S extends SearchBookOption<?>> {
    Flux<GetBookResponse> process(final S searchBookOption);

    boolean support(final String type);
}
