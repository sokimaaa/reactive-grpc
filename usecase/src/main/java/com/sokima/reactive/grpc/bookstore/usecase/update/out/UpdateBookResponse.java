package com.sokima.reactive.grpc.bookstore.usecase.update.out;

import com.sokima.reactive.grpc.bookstore.usecase.update.in.BookFieldOption;
import org.immutables.value.Value;

@Value.Immutable
public interface UpdateBookResponse {
    String checksum();

    BookFieldOption newField();

    BookFieldOption oldField();
}
