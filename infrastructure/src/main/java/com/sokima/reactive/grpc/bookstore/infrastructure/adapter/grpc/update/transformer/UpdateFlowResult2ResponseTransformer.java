package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update.transformer;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Java2ProtoTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.ProtoChecksumTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update.field.FieldResponseOneof;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.update.out.UpdateBookFlowResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UpdateFlowResult2ResponseTransformer implements Java2ProtoTransformer<UpdateBookResponse, UpdateBookFlowResult> {

    private static final Logger log = LoggerFactory.getLogger(UpdateFlowResult2ResponseTransformer.class);

    private final ProtoChecksumTransformer checksumTransformer;
    private final FieldResponseOneof fieldResponseOneof;

    public UpdateFlowResult2ResponseTransformer(final ProtoChecksumTransformer checksumTransformer, final FieldResponseOneof fieldResponseOneof) {
        this.checksumTransformer = checksumTransformer;
        this.fieldResponseOneof = fieldResponseOneof;
    }

    @Override
    public UpdateBookResponse transform(final UpdateBookFlowResult pojo) {
        log.trace("Transforming to proto update book response: {}", pojo);
        return UpdateBookResponse.newBuilder()
                .setChecksum(checksumTransformer.transform(pojo.checksum()))
                .setNewBookField(fieldResponseOneof.resolve(pojo.newField()))
                .setOldBookField(fieldResponseOneof.resolve(pojo.oldField()))
                .build();
    }
}
