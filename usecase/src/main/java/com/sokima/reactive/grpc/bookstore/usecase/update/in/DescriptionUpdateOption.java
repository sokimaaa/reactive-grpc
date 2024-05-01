package com.sokima.reactive.grpc.bookstore.usecase.update.in;

import com.sokima.reactive.grpc.bookstore.domain.helper.UpdatableField;
import org.immutables.value.Value;

@Value.Immutable
public interface DescriptionUpdateOption extends UpdateOption {
    @Value.Default
    default String field() {
        return UpdatableField.DESCRIPTION.name();
    }
}
