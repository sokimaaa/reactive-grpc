package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.transformer;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Java2ProtoTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.ProtoChecksumTransformer;
import com.sokima.reactive.grpc.bookstore.proto.GetBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookFlowResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GetFlowResult2ResponseTransformer implements Java2ProtoTransformer<GetBookResponse, GetBookFlowResult> {

    private static final Logger log = LoggerFactory.getLogger(GetFlowResult2ResponseTransformer.class);

    private final ProtoChecksumTransformer checksumTransformer;

    public GetFlowResult2ResponseTransformer(final ProtoChecksumTransformer checksumTransformer) {
        this.checksumTransformer = checksumTransformer;
    }

    @Override
    public GetBookResponse transform(final GetBookFlowResult pojo) {
        log.trace("Transforming to proto get book response: {}", pojo);
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
