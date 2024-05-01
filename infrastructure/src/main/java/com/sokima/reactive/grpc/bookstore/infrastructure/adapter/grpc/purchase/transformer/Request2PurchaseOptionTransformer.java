package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.purchase.transformer;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.purchase.option.PurchaseOptionOneof;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Proto2JavaTransformer;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.PurchaseOption;
import org.springframework.stereotype.Component;

@Component
public class Request2PurchaseOptionTransformer implements Proto2JavaTransformer<PurchaseBookRequest, PurchaseOption<?>> {

    private final PurchaseOptionOneof<?> purchaseOptionOneof;

    public Request2PurchaseOptionTransformer(final PurchaseOptionOneof<?> purchaseOptionOneof) {
        this.purchaseOptionOneof = purchaseOptionOneof;
    }

    @Override
    public PurchaseOption<?> transform(final PurchaseBookRequest proto) {
        return purchaseOptionOneof.resolve(proto);
    }
}
