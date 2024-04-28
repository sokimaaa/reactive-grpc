package com.sokima.reactive.grpc.bookstore.usecase.update.resolver;

import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.oneof.OneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.BookFieldOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.ImmutableDescriptionBookFieldOption;

public class DescriptionUpdateOptionOneofResolver implements UpdateOptionOneofResolver {
    private final UpdateOptionOneofResolver next;

    public DescriptionUpdateOptionOneofResolver(final UpdateOptionOneofResolver next) {
        this.next = next;
    }

    @Override
    public BookFieldOption resolve(final UpdateBookRequest oneof) {
        if (oneof.hasUpdatedBookField() && oneof.getUpdatedBookField().hasDescription()) {
            return ImmutableDescriptionBookFieldOption.builder()
                    .value(oneof.getUpdatedBookField().getDescription())
                    .checksum(checksum(oneof))
                    .build();
        }
        return next().resolve(oneof);
    }

    @Override
    public OneofResolver<UpdateBookRequest, BookFieldOption> next() {
        return this.next;
    }
}
