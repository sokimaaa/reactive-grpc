package com.sokima.reactive.grpc.bookstore.usecase.update.out;

import com.sokima.reactive.grpc.bookstore.usecase.update.in.UpdateOption;
import org.immutables.value.Value;

@Value.Immutable
public interface UpdateBookFlowResult {
    String checksum();

    UpdateOption newField();

    UpdateOption oldField();
}
