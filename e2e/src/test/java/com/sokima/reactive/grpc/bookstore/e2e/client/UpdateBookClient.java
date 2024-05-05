package com.sokima.reactive.grpc.bookstore.e2e.client;

import com.sokima.reactive.grpc.bookstore.proto.BookField;
import com.sokima.reactive.grpc.bookstore.proto.ReactorUpdateBookUseCaseGrpc;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Scope("prototype")
public class UpdateBookClient {

    private final ReactorUpdateBookUseCaseGrpc.ReactorUpdateBookUseCaseStub stub;

    private UpdateBookRequest updateBookRequest;

    public UpdateBookClient(final ReactorUpdateBookUseCaseGrpc.ReactorUpdateBookUseCaseStub stub) {
        this.stub = stub;
    }

    public UpdateBookClient setDescriptionRequest(
            final String title, final String author, final String edition, final String description
    ) {
        this.updateBookRequest = UpdateBookRequest.newBuilder()
                .setTitle(title)
                .setAuthor(author)
                .setEdition(edition)
                .setUpdatedBookField(
                        BookField.newBuilder()
                                .setDescription(description)
                                .build()
                )
                .build();
        return this;
    }

    public Mono<UpdateBookResponse> invokeUpdateBookStub() {
        return stub.updateBook(Mono.just(updateBookRequest));
    }
}
