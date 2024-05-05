package com.sokima.reactive.grpc.bookstore.e2e.client;

import com.sokima.reactive.grpc.bookstore.proto.ReactorSupplyBookUseCaseGrpc;
import com.sokima.reactive.grpc.bookstore.proto.SupplyBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.SupplyBookResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class SupplyBookClient {
    private final ReactorSupplyBookUseCaseGrpc.ReactorSupplyBookUseCaseStub stub;

    private final List<SupplyBookRequest> supplyBookRequestList = new ArrayList<>();

    public SupplyBookClient(final ReactorSupplyBookUseCaseGrpc.ReactorSupplyBookUseCaseStub stub) {
        this.stub = stub;
    }

    public SupplyBookClient setSupplyBookRequest(
            final String title, final String author, final String edition, final Integer supplyNumber
    ) {
        this.supplyBookRequestList.add(
                SupplyBookRequest.newBuilder()
                        .setTitle(title)
                        .setAuthor(author)
                        .setEdition(edition)
                        .setSupplyNumber(supplyNumber)
                        .build()
        );
        return this;
    }

    public Flux<SupplyBookResponse> invokeSupplyBookStub() {
        return stub.supplyBooks(Flux.fromIterable(supplyBookRequestList));
    }
}
