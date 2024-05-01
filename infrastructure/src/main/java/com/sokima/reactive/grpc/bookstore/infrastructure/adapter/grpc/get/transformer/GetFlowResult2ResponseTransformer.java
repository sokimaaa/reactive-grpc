package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.transformer;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Java2ProtoTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.ProtoChecksumTransformer;
import com.sokima.reactive.grpc.bookstore.proto.GetBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookFlowResult;
import org.springframework.stereotype.Component;

@Component
public class GetFlowResult2ResponseTransformer implements Java2ProtoTransformer<GetBookResponse, GetBookFlowResult> {

    private final ProtoChecksumTransformer checksumTransformer;

    public GetFlowResult2ResponseTransformer(final ProtoChecksumTransformer checksumTransformer) {
        this.checksumTransformer = checksumTransformer;
    }

    @Override
    public GetBookResponse transform(final GetBookFlowResult pojo) {
        return GetBookResponse.newBuilder()
                .setChecksum(checksumTransformer.transform(pojo.checksum()))
                .setTitle(pojo.title())
                .setAuthor(pojo.author())
                .setEdition(pojo.edition())
                .setDescription(pojo.description())
                .setIsAvailable(pojo.isAvailable())
                .build();
    }
}
