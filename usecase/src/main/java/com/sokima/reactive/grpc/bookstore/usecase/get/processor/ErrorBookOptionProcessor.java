package com.sokima.reactive.grpc.bookstore.usecase.get.processor;

import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchBookOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookResponse;
import reactor.core.publisher.Flux;

public class ErrorBookOptionProcessor implements BookOptionProcessor<SearchBookOption<Object>> {
    @Override
    public Flux<GetBookResponse> process(SearchBookOption<Object> searchBookOption) {
        throw new UnsupportedOperationException("Fallback processor. Unknown type of search book option: " + searchBookOption.option());
    }

    @Override
    public boolean support(final String type) {
        return false;
    }
}
