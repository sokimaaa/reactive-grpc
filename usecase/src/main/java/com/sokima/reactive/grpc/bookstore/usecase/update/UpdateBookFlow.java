package com.sokima.reactive.grpc.bookstore.usecase.update;

import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.UpdateOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.out.UpdateBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.update.processor.ErrorUpdateOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.update.processor.UpdateOptionProcessor;
import reactor.core.publisher.Flux;

import java.util.List;

public final class UpdateBookFlow implements Flow<UpdateOption, UpdateBookFlowResult> {

    private final List<UpdateOptionProcessor<? extends UpdateOption>> processors;
    private final ErrorUpdateOptionProcessor fallbackProcessor;

    public UpdateBookFlow(
            final List<UpdateOptionProcessor<? extends UpdateOption>> processors,
            final ErrorUpdateOptionProcessor fallbackProcessor) {
        this.processors = processors;
        this.fallbackProcessor = fallbackProcessor;
    }

    @Override
    public Flux<UpdateBookFlowResult> doFlow(final UpdateOption option) {
        return processors.stream()
                .filter(processor -> processor.support(option.field()))
                .findFirst()
                .orElse(fallbackProcessor)
                .safeCastAndProcess(option);
    }
}
