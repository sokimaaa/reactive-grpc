package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.purchase;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.Workflow;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.purchase.transformer.ResponseList2ResponseTransformer;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookResponse;
import com.sokima.reactive.grpc.bookstore.proto.ReactorPurchaseBookUseCaseGrpc;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@GrpcService
public class PurchaseBookGrpcAdapter extends ReactorPurchaseBookUseCaseGrpc.PurchaseBookUseCaseImplBase {

    private static final Logger log = LoggerFactory.getLogger(PurchaseBookGrpcAdapter.class);

    private final Workflow<PurchaseBookRequest, PurchaseBookResponse> purchaseBookWorkflow;

    private final ResponseList2ResponseTransformer transformer;

    public PurchaseBookGrpcAdapter(
            final Workflow<PurchaseBookRequest, PurchaseBookResponse> purchaseBookWorkflow,
            final ResponseList2ResponseTransformer transformer
    ) {
        this.purchaseBookWorkflow = purchaseBookWorkflow;
        this.transformer = transformer;
    }

    @Override
    public Mono<PurchaseBookResponse> purchaseBooks(final Flux<PurchaseBookRequest> request) {
        return request.doOnNext(req -> log.debug("Inbound grpc request: {}", req))
                .flatMap(purchaseBookWorkflow::doWorkflow)
                .collectList()
                .doOnNext(purchaseBookResponses -> log.debug("Purchase book responses after collect: {}", purchaseBookResponses))
                .map(transformer::mergePurchaseBookResponses)
                .doOnNext(resp -> log.debug("Outbound grpc response: {}", resp));
    }
}
