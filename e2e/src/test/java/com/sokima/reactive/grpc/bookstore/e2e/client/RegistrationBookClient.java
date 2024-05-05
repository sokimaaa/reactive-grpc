package com.sokima.reactive.grpc.bookstore.e2e.client;

import com.sokima.reactive.grpc.bookstore.proto.ReactorRegistrationBookUseCaseGrpc;
import com.sokima.reactive.grpc.bookstore.proto.RegistrationBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.RegistrationBookResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Scope("prototype")
public class RegistrationBookClient {
    private final ReactorRegistrationBookUseCaseGrpc.ReactorRegistrationBookUseCaseStub stub;

    private RegistrationBookRequest registrationBookRequest;

    public RegistrationBookClient(final ReactorRegistrationBookUseCaseGrpc.ReactorRegistrationBookUseCaseStub stub) {
        this.stub = stub;
    }

    public RegistrationBookClient setRegistrationBookRequest(
            final String title, final String author, final String edition
    ) {
        this.registrationBookRequest = RegistrationBookRequest.newBuilder()
                .setTitle(title)
                .setAuthor(author)
                .setEdition(edition)
                .build();
        return this;
    }

    public Mono<RegistrationBookResponse> invokeRegistrationBookStub() {
        return stub.registerBook(Mono.just(registrationBookRequest));
    }
}
