package com.sokima.reactive.grpc.bookstore.usecase.get;

import com.sokima.reactive.grpc.bookstore.domain.oneof.OneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.GetBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchBookOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.get.processor.BookOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.get.processor.ErrorBookOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.get.resolver.option.SearchOptionOneofResolver;
import reactor.core.publisher.Flux;

import java.util.List;

public final class GetBookFacade {
    private final List<BookOptionProcessor<?>> processors;
    private final ErrorBookOptionProcessor fallbackProcessor;
    private final OneofResolver<GetBookRequest, SearchBookOption<?>> searchOptionOneofResolver;

    public GetBookFacade(final List<BookOptionProcessor<?>> processors,
                         final ErrorBookOptionProcessor fallbackProcessor,
                         final OneofResolver<GetBookRequest, SearchBookOption<?>> searchOptionOneofResolver) {
        this.processors = processors;
        this.fallbackProcessor = fallbackProcessor;
        this.searchOptionOneofResolver = searchOptionOneofResolver;
    }

    public Flux<GetBookResponse> doGet(final GetBookRequest getBookRequest) {
        final SearchBookOption<?> option = searchOptionOneofResolver.resolve(getBookRequest);
        final BookOptionProcessor processor = processors.stream()
                .filter(proc -> proc.support(option.type()))
                .findFirst()
                .orElse(fallbackProcessor);
        return processor.process(option);
    }
}
