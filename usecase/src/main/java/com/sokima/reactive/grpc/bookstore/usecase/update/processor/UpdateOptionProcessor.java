package com.sokima.reactive.grpc.bookstore.usecase.update.processor;

import com.sokima.reactive.grpc.bookstore.usecase.update.in.UpdateOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.out.UpdateBookFlowResult;
import reactor.core.publisher.Flux;

public interface UpdateOptionProcessor<F extends UpdateOption> {
    Flux<UpdateBookFlowResult> process(final F bookFieldOption);

    boolean support(final String field);

    @SuppressWarnings("unchecked")
    default Flux<UpdateBookFlowResult> safeCastAndProcess(final UpdateOption updateOption) {
        return this.process((F) updateOption);
    }
}
