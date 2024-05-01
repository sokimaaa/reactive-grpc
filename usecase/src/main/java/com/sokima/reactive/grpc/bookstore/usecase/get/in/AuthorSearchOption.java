package com.sokima.reactive.grpc.bookstore.usecase.get.in;

import com.sokima.reactive.grpc.bookstore.domain.helper.BookIdentificationOption;
import org.immutables.value.Value;

@Value.Immutable
public interface AuthorSearchOption extends SearchOption<String> {
    /**
     * @return the author value.
     */
    String option();

    @Value.Default
    default String type() {
        return BookIdentificationOption.AUTHOR.name();
    }
}
