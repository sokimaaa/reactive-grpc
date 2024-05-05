package com.sokima.reactive.grpc.bookstore.e2e.client;

import com.sokima.reactive.grpc.bookstore.proto.FullBookMetadata;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookResponse;
import com.sokima.reactive.grpc.bookstore.proto.ReactorPurchaseBookUseCaseGrpc;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class PurchaseBookClient {
    private final ReactorPurchaseBookUseCaseGrpc.ReactorPurchaseBookUseCaseStub stub;

    private final List<PurchaseBookRequest> purchaseBookRequestList = new ArrayList<>();

    public PurchaseBookClient(final ReactorPurchaseBookUseCaseGrpc.ReactorPurchaseBookUseCaseStub stub) {
        this.stub = stub;
    }

    public PurchaseBookClient setFullMetadataRequest(
            final String title, final String author, final String edition
    ) {
        this.purchaseBookRequestList.add(
                PurchaseBookRequest.newBuilder()
                        .setFullBookMetadata(
                                FullBookMetadata.newBuilder()
                                        .setTitle(title)
                                        .setAuthor(author)
                                        .setEdition(edition)
                                        .build()
                        )
                        .build()
        );
        return this;
    }

    public Mono<PurchaseBookResponse> invokePurchaseBookStub() {
        return stub.purchaseBooks(Flux.fromIterable(purchaseBookRequestList));
    }
}
