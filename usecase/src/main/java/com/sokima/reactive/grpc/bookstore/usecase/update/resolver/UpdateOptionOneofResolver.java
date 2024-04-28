package com.sokima.reactive.grpc.bookstore.usecase.update.resolver;

import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.oneof.OneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.BookFieldOption;

public interface UpdateOptionOneofResolver extends OneofResolver<UpdateBookRequest, BookFieldOption> {
    default String checksum(final UpdateBookRequest updateBookRequest) {
        return ChecksumGenerator.generateBookChecksum(
                updateBookRequest.getTitle(),
                updateBookRequest.getAuthor(),
                updateBookRequest.getEdition()
        );
    }
}
