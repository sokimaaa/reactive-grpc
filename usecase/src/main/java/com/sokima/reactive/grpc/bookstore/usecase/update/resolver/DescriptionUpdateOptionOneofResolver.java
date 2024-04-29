package com.sokima.reactive.grpc.bookstore.usecase.update.resolver;

import com.sokima.reactive.grpc.bookstore.domain.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.BookFieldOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.ImmutableDescriptionBookFieldOption;

public class DescriptionUpdateOptionOneofResolver extends AbstractOneofResolver<UpdateBookRequest, BookFieldOption>
        implements UpdateOptionOneofResolver {
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
}
