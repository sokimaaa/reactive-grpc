package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.registration;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.Workflow;
import com.sokima.reactive.grpc.bookstore.proto.ReactorRegistrationBookUseCaseGrpc;
import com.sokima.reactive.grpc.bookstore.proto.RegistrationBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.RegistrationBookResponse;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

@GrpcService
public class RegistrationBookGrpcAdapter extends ReactorRegistrationBookUseCaseGrpc.RegistrationBookUseCaseImplBase {

    private static final Logger log = LoggerFactory.getLogger(RegistrationBookGrpcAdapter.class);

    private final Workflow<RegistrationBookRequest, RegistrationBookResponse> registrationBookWorkflow;

    public RegistrationBookGrpcAdapter(final Workflow<RegistrationBookRequest, RegistrationBookResponse> registrationBookWorkflow) {
        this.registrationBookWorkflow = registrationBookWorkflow;
    }

    @Override
    public Mono<RegistrationBookResponse> registerBook(final Mono<RegistrationBookRequest> request) {
        return request.doOnNext(req -> log.debug("Inbound grpc request: {}", req))
                .flatMapMany(registrationBookWorkflow::doWorkflow).single()
                .doOnNext(resp -> log.debug("Outbound grpc response: {}", resp));
    }
}
