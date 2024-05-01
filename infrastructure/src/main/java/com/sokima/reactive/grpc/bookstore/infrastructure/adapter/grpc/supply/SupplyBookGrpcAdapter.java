package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.supply;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.Workflow;
import com.sokima.reactive.grpc.bookstore.proto.ReactorSupplyBookUseCaseGrpc;
import com.sokima.reactive.grpc.bookstore.proto.SupplyBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.SupplyBookResponse;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Flux;

@GrpcService
public class SupplyBookGrpcAdapter extends ReactorSupplyBookUseCaseGrpc.SupplyBookUseCaseImplBase {

    private final Workflow<SupplyBookRequest, SupplyBookResponse> supplyBookWorkflow;

    public SupplyBookGrpcAdapter(final Workflow<SupplyBookRequest, SupplyBookResponse> supplyBookWorkflow) {
        this.supplyBookWorkflow = supplyBookWorkflow;
    }

    @Override
    public Flux<SupplyBookResponse> supplyBooks(final Flux<SupplyBookRequest> requestFlux) {
        return requestFlux.flatMap(supplyBookWorkflow::doWorkflow);
    }
}
