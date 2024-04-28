package com.sokima.reactive.grpc.bookstore.usecase.get.in;

import com.sokima.reactive.grpc.bookstore.domain.helper.OneofOptions;
import org.immutables.value.Value;

@Value.Immutable
public interface TitleBookOption extends SearchBookOption<String> {
    /**
     * @return the title value.
     */
    String option();

    @Value.Default
    default String type() {
        return OneofOptions.TITLE.name();
    }
}
