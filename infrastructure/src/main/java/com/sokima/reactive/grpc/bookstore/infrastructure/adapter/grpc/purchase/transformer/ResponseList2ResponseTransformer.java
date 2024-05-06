package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.purchase.transformer;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.GrpcCollectors;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookResponse;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class ResponseList2ResponseTransformer {
    public PurchaseBookResponse mergePurchaseBookResponses(final List<PurchaseBookResponse> responseList) {
        return responseList.stream()
                .map(PurchaseBookResponse::getPurchasedBooksList)
                .flatMap(Collection::stream)
                .collect(GrpcCollectors.toPurchaseBookResponse());
    }
}
