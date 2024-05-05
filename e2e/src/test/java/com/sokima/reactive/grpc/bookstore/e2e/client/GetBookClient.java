package com.sokima.reactive.grpc.bookstore.e2e.client;

import com.sokima.reactive.grpc.bookstore.proto.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Scope("prototype")
public class GetBookClient {
    private final ReactorGetBookUseCaseGrpc.ReactorGetBookUseCaseStub stub;

    private GetBookRequest getBookRequest;

    public GetBookClient(final ReactorGetBookUseCaseGrpc.ReactorGetBookUseCaseStub stub) {
        this.stub = stub;
    }

    public GetBookClient setFullMetadataRequest(final String title, final String author, final String edition) {
        this.getBookRequest = GetBookRequest.newBuilder()
                .setFullBookMetadata(
                        FullBookMetadata.newBuilder()
                                .setTitle(title)
                                .setAuthor(author)
                                .setEdition(edition)
                                .build()
                )
                .build();
        return this;
    }

    public GetBookClient setAuthorPartialMetadataRequest(final String author) {
        this.getBookRequest = GetBookRequest.newBuilder()
                .setPartialBookMetadata(
                        PartialBookMetadata.newBuilder()
                                .setAuthor(author)
                                .build()
                )
                .build();
        return this;
    }

    public GetBookClient setTitlePartialMetadataRequest(final String title) {
        this.getBookRequest = GetBookRequest.newBuilder()
                .setPartialBookMetadata(
                        PartialBookMetadata.newBuilder()
                                .setTitle(title)
                                .build()
                )
                .build();
        return this;
    }

    public GetBookClient setChecksumRequest(final String checksum) {
        this.getBookRequest = GetBookRequest.newBuilder()
                .setBookChecksum(
                        Checksum.newBuilder()
                                .setValue(checksum)
                                .build()
                )
                .build();
        return this;
    }

    public Flux<GetBookResponse> invokeGetBookStub() {
        return stub.getBooks(Mono.just(getBookRequest));
    }
}
