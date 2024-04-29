package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc;

import com.sokima.reactive.grpc.bookstore.proto.Checksum;
import com.sokima.reactive.grpc.bookstore.proto.ReactorRegistrationBookUseCaseGrpc;
import com.sokima.reactive.grpc.bookstore.proto.RegistrationBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.RegistrationBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.registration.RegistrationBookFacade;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;

@GrpcService
public class RegistrationBookGrpcAdapter extends ReactorRegistrationBookUseCaseGrpc.RegistrationBookUseCaseImplBase {

    private final RegistrationBookFacade registrationBookFacade;

    public RegistrationBookGrpcAdapter(final RegistrationBookFacade registrationBookFacade) {
        this.registrationBookFacade = registrationBookFacade;
    }

    @Override
    public Mono<RegistrationBookResponse> registerBook(final Mono<RegistrationBookRequest> request) {
        return request.flatMap(registrationBookFacade::doRegistration)
                .map(response -> RegistrationBookResponse.newBuilder()
                        .setChecksum(
                                Checksum.newBuilder()
                                        .setValue(response.checksum())
                                        .build()
                        )
                        .build());
    }
}
