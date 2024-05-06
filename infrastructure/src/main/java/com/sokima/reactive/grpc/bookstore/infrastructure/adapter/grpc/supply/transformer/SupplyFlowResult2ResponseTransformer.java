package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.supply.transformer;

import com.sokima.reactive.grpc.bookstore.domain.Isbn;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.GrpcCollectors;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Java2ProtoTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.ProtoIsbnTransformer;
import com.sokima.reactive.grpc.bookstore.proto.SupplyBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.supply.out.SupplyBookFlowResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SupplyFlowResult2ResponseTransformer implements Java2ProtoTransformer<SupplyBookResponse, SupplyBookFlowResult> {

    private static final Logger log = LoggerFactory.getLogger(SupplyFlowResult2ResponseTransformer.class);

    private final ProtoIsbnTransformer isbnTransformer;

    public SupplyFlowResult2ResponseTransformer(final ProtoIsbnTransformer isbnTransformer) {
        this.isbnTransformer = isbnTransformer;
    }

    @Override
    public SupplyBookResponse transform(final SupplyBookFlowResult pojo) {
        log.trace("Transforming to proto supply book response: {}", pojo);
        return pojo.isbns()
                .stream()
                .map(Isbn::isbn)
                .map(isbnTransformer::transform)
                .collect(GrpcCollectors.toSupplyBookResponse(pojo.checksum()));
    }
}
