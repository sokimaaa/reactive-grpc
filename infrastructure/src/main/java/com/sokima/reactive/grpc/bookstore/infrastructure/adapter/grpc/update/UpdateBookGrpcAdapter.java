package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.Workflow;
import com.sokima.reactive.grpc.bookstore.proto.ReactorUpdateBookUseCaseGrpc;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookResponse;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

@GrpcService
public class UpdateBookGrpcAdapter extends ReactorUpdateBookUseCaseGrpc.UpdateBookUseCaseImplBase {

    private static final Logger log = LoggerFactory.getLogger(UpdateBookGrpcAdapter.class);

    private final Workflow<UpdateBookRequest, UpdateBookResponse> updateBookWorkflow;

    public UpdateBookGrpcAdapter(final Workflow<UpdateBookRequest, UpdateBookResponse> updateBookWorkflow) {
        this.updateBookWorkflow = updateBookWorkflow;
    }

    @Override
    public Mono<UpdateBookResponse> updateBook(final Mono<UpdateBookRequest> request) {
        return request.doOnNext(req -> log.debug("Inbound grpc request: {}", req))
                .flatMapMany(updateBookWorkflow::doWorkflow).single()
                .doOnNext(resp -> log.debug("Outbound grpc response: {}", resp));
    }
}
