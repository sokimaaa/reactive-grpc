package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update.option;

import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof.OneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.UpdateOption;

public interface UpdateOptionOneof extends OneofResolver<UpdateBookRequest, UpdateOption> {
    default String checksum(final UpdateBookRequest updateBookRequest) {
        return ChecksumGenerator.generateBookChecksum(
                updateBookRequest.getTitle(),
                updateBookRequest.getAuthor(),
                updateBookRequest.getEdition()
        );
    }
}
