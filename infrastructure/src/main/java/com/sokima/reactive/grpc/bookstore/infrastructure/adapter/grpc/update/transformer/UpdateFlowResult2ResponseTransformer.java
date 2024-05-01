package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update.transformer;

import com.sokima.reactive.grpc.bookstore.domain.helper.FieldOption;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Java2ProtoTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.ProtoChecksumTransformer;
import com.sokima.reactive.grpc.bookstore.proto.BookField;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.UpdateOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.out.UpdateBookFlowResult;
import org.springframework.stereotype.Component;

@Component
public class UpdateFlowResult2ResponseTransformer implements Java2ProtoTransformer<UpdateBookResponse, UpdateBookFlowResult> {

    private final ProtoChecksumTransformer checksumTransformer;

    public UpdateFlowResult2ResponseTransformer(final ProtoChecksumTransformer checksumTransformer) {
        this.checksumTransformer = checksumTransformer;
    }

    @Override
    public UpdateBookResponse transform(final UpdateBookFlowResult pojo) {
        return UpdateBookResponse.newBuilder()
                .setChecksum(checksumTransformer.transform(pojo.checksum()))
                .setNewBookField(bookField(pojo.newField()))
                .setOldBookField(bookField(pojo.oldField()))
                .build();
    }

    private BookField bookField(final UpdateOption option) {
        final var builder = BookField.newBuilder();
        if (FieldOption.DESCRIPTION.name().equals(option.field())) {
            return builder.setDescription(option.value())
                    .build();
        }
        return builder.build();
    }
}
