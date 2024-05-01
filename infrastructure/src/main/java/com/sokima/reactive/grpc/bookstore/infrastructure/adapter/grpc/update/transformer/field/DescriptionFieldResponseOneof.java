package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update.transformer.field;

import com.sokima.reactive.grpc.bookstore.domain.helper.UpdatableField;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.BookField;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.UpdateOption;
import org.springframework.stereotype.Component;

@Component
public class DescriptionFieldResponseOneof extends AbstractOneofResolver<UpdateOption, BookField>
        implements FieldResponseOneof {
    @Override
    protected boolean condition(final UpdateOption oneof) {
        return UpdatableField.DESCRIPTION.name().equals(oneof.field());
    }

    @Override
    protected BookField result(final UpdateOption oneof) {
        return BookField.newBuilder()
                .setDescription(oneof.value())
                .build();
    }
}
