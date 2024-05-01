package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.Workflow;
import com.sokima.reactive.grpc.bookstore.proto.ReactorUpdateBookUseCaseGrpc;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookResponse;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;

@GrpcService
public class UpdateBookGrpcAdapter extends ReactorUpdateBookUseCaseGrpc.UpdateBookUseCaseImplBase {

    private final Workflow<UpdateBookRequest, UpdateBookResponse> updateBookWorkflow;

    public UpdateBookGrpcAdapter(final Workflow<UpdateBookRequest, UpdateBookResponse> updateBookWorkflow) {
        this.updateBookWorkflow = updateBookWorkflow;
    }

    @Override
    public Mono<UpdateBookResponse> updateBook(final Mono<UpdateBookRequest> request) {
        return request.flatMapMany(updateBookWorkflow::doWorkflow).single();
    }
}
