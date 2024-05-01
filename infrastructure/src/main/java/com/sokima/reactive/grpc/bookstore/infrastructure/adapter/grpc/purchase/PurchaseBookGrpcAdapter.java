package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.purchase;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.Workflow;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookResponse;
import com.sokima.reactive.grpc.bookstore.proto.ReactorPurchaseBookUseCaseGrpc;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@GrpcService
public class PurchaseBookGrpcAdapter extends ReactorPurchaseBookUseCaseGrpc.PurchaseBookUseCaseImplBase {

    private final Workflow<PurchaseBookRequest, PurchaseBookResponse> purchaseBookWorkflow;

    public PurchaseBookGrpcAdapter(final Workflow<PurchaseBookRequest, PurchaseBookResponse> purchaseBookWorkflow) {
        this.purchaseBookWorkflow = purchaseBookWorkflow;
    }

    @Override
    public Mono<PurchaseBookResponse> purchaseBooks(final Flux<PurchaseBookRequest> request) {
        return request.flatMap(purchaseBookWorkflow::doWorkflow).single();
    }
}
