package com.sokima.reactive.grpc.bookstore.usecase.update.processor;

import com.sokima.reactive.grpc.bookstore.usecase.update.in.BookFieldOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.out.UpdateBookResponse;
import reactor.core.publisher.Flux;

public class ErrorUpdateOptionProcessor implements UpdateOptionProcessor<BookFieldOption> {

    @Override
    public Flux<UpdateBookResponse> process(final BookFieldOption bookFieldOption) {
        throw new UnsupportedOperationException("Fallback processor. Unknown type of search book option: " + bookFieldOption.checksum());
    }

    @Override
    public boolean support(final String field) {
        return false;
    }
}
