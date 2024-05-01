package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.registration.transformer;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Java2ProtoTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.ProtoChecksumTransformer;
import com.sokima.reactive.grpc.bookstore.proto.RegistrationBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.registration.out.RegistrationBookFlowResult;
import org.springframework.stereotype.Component;

@Component
public class RegistrationFlowResult2ResponseTransformer implements Java2ProtoTransformer<RegistrationBookResponse, RegistrationBookFlowResult> {

    private final ProtoChecksumTransformer checksumTransformer;

    public RegistrationFlowResult2ResponseTransformer(final ProtoChecksumTransformer checksumTransformer) {
        this.checksumTransformer = checksumTransformer;
    }

    @Override
    public RegistrationBookResponse transform(final RegistrationBookFlowResult pojo) {
        return RegistrationBookResponse.newBuilder()
                .setChecksum(checksumTransformer.transform(pojo.checksum()))
                .build();
    }
}
