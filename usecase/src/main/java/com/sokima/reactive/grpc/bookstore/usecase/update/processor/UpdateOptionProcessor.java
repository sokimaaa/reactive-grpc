package com.sokima.reactive.grpc.bookstore.usecase.update.processor;

import com.sokima.reactive.grpc.bookstore.usecase.update.in.BookFieldOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.out.UpdateBookResponse;
import reactor.core.publisher.Flux;

public interface UpdateOptionProcessor<F extends BookFieldOption> {
    Flux<UpdateBookResponse> process(final F bookFieldOption);

    boolean support(final String field);
}
