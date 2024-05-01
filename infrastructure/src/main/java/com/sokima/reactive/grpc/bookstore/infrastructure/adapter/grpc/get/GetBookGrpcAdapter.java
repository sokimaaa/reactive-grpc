package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.Workflow;
import com.sokima.reactive.grpc.bookstore.proto.GetBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.GetBookResponse;
import com.sokima.reactive.grpc.bookstore.proto.ReactorGetBookUseCaseGrpc;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@GrpcService
public class GetBookGrpcAdapter extends ReactorGetBookUseCaseGrpc.GetBookUseCaseImplBase {

    private final Workflow<GetBookRequest, GetBookResponse> getBookWorkflow;

    public GetBookGrpcAdapter(final Workflow<GetBookRequest, GetBookResponse> getBookWorkflow) {
        this.getBookWorkflow = getBookWorkflow;
    }

    @Override
    public Flux<GetBookResponse> getBooks(final Mono<GetBookRequest> request) {
        return request.flatMapMany(getBookWorkflow::doWorkflow);
    }
}
