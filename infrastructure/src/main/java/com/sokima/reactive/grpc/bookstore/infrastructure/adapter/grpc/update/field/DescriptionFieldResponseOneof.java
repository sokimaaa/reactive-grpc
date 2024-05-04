package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update.field;

import com.sokima.reactive.grpc.bookstore.domain.helper.UpdatableField;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.BookField;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.UpdateOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DescriptionFieldResponseOneof extends AbstractOneofResolver<UpdateOption, BookField>
        implements FieldResponseOneof {

    private static final Logger log = LoggerFactory.getLogger(DescriptionFieldResponseOneof.class);

    @Override
    protected boolean condition(final UpdateOption oneof) {
        log.trace("Passed description field response option resolver with: {}", oneof);
        return UpdatableField.DESCRIPTION.name().equals(oneof.field());
    }

    @Override
    protected BookField result(final UpdateOption oneof) {
        log.debug("Oneof is resolved as Description Field Response Option: {}", oneof);
        return BookField.newBuilder()
                .setDescription(oneof.value())
                .build();
    }
}
