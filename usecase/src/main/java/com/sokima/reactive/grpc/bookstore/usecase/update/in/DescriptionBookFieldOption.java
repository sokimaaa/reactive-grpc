package com.sokima.reactive.grpc.bookstore.usecase.update.in;

import com.sokima.reactive.grpc.bookstore.domain.helper.FieldOption;
import org.immutables.value.Value;

@Value.Immutable
public interface DescriptionBookFieldOption extends BookFieldOption {
    @Value.Default
    default String field() {
        return FieldOption.DESCRIPTION.name();
    }
}
