package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.registration;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.Workflow;
import com.sokima.reactive.grpc.bookstore.proto.ReactorRegistrationBookUseCaseGrpc;
import com.sokima.reactive.grpc.bookstore.proto.RegistrationBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.RegistrationBookResponse;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;

@GrpcService
public class RegistrationBookGrpcAdapter extends ReactorRegistrationBookUseCaseGrpc.RegistrationBookUseCaseImplBase {

    private final Workflow<RegistrationBookRequest, RegistrationBookResponse> registrationBookWorkflow;

    public RegistrationBookGrpcAdapter(final Workflow<RegistrationBookRequest, RegistrationBookResponse> registrationBookWorkflow) {
        this.registrationBookWorkflow = registrationBookWorkflow;
    }

    @Override
    public Mono<RegistrationBookResponse> registerBook(final Mono<RegistrationBookRequest> request) {
        return request.flatMapMany(registrationBookWorkflow::doWorkflow).single();
    }
}
