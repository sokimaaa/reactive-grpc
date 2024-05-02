package com.sokima.reactive.grpc.bookstore.usecase.update.processor;

import com.sokima.reactive.grpc.bookstore.usecase.update.in.UpdateOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.out.UpdateBookFlowResult;
import reactor.core.publisher.Flux;

public class ErrorUpdateOptionProcessor implements UpdateOptionProcessor<UpdateOption> {

    @Override
    public Flux<UpdateBookFlowResult> process(final UpdateOption updateOption) {
        throw new UnsupportedOperationException("Fallback processor. Unknown type of update option: " + updateOption.field());
    }

    @Override
    public boolean support(final String field) {
        return false;
    }
}
