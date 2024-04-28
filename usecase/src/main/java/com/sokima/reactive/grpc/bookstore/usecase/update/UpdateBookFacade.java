package com.sokima.reactive.grpc.bookstore.usecase.update;

import com.sokima.reactive.grpc.bookstore.proto.UpdateBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.BookFieldOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.out.UpdateBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.update.processor.ErrorUpdateOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.update.processor.UpdateOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.update.resolver.UpdateOptionOneofResolver;
import reactor.core.publisher.Mono;

import java.util.List;

public final class UpdateBookFacade {

    private final UpdateOptionOneofResolver resolver;
    private final List<UpdateOptionProcessor<?>> processors;
    private final ErrorUpdateOptionProcessor fallbackProcessor;

    public UpdateBookFacade(final UpdateOptionOneofResolver resolver,
                            final List<UpdateOptionProcessor<?>> processors,
                            final ErrorUpdateOptionProcessor fallbackProcessor) {
        this.resolver = resolver;
        this.processors = processors;
        this.fallbackProcessor = fallbackProcessor;
    }

    public Mono<UpdateBookResponse> updateField(final UpdateBookRequest updateBookRequest) {
        final BookFieldOption option = resolver.resolve(updateBookRequest);
        final UpdateOptionProcessor updateOptionProcessor = processors.stream()
                .filter(processor -> processor.support(option.field()))
                .findFirst()
                .orElse(fallbackProcessor);
        return updateOptionProcessor.process(option).single();
    }
}
