package com.sokima.reactive.grpc.bookstore.usecase.get;

import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.get.processor.BookOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.get.processor.ErrorBookOptionProcessor;
import reactor.core.publisher.Flux;

import java.util.List;

public final class GetBookFlow implements Flow<SearchOption<?>, GetBookFlowResult> {
    private final List<BookOptionProcessor<? extends SearchOption<?>>> processors;
    private final ErrorBookOptionProcessor<SearchOption<?>> fallbackProcessor;

    public GetBookFlow(
            final List<BookOptionProcessor<? extends SearchOption<?>>> processors,
            final ErrorBookOptionProcessor<SearchOption<?>> fallbackProcessor) {
        this.processors = processors;
        this.fallbackProcessor = fallbackProcessor;
    }

    @Override
    public Flux<GetBookFlowResult> doFlow(final SearchOption<?> searchOption) {
        return processors.stream()
                .filter(processor -> processor.support(searchOption.type()))
                .findFirst()
                .orElse(fallbackProcessor)
                .safeCastAndProcess(searchOption);
    }
}
