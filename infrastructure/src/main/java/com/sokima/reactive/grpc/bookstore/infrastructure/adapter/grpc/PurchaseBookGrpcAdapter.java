package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc;

import com.sokima.reactive.grpc.bookstore.proto.ISBN;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookResponse;
import com.sokima.reactive.grpc.bookstore.proto.ReactorPurchaseBookUseCaseGrpc;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.PurchaseBookFacade;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@GrpcService
public class PurchaseBookGrpcAdapter extends ReactorPurchaseBookUseCaseGrpc.PurchaseBookUseCaseImplBase {

    private final PurchaseBookFacade purchaseBookFacade;

    public PurchaseBookGrpcAdapter(final PurchaseBookFacade purchaseBookFacade) {
        this.purchaseBookFacade = purchaseBookFacade;
    }

    @Override
    public Mono<PurchaseBookResponse> purchaseBooks(final Flux<PurchaseBookRequest> request) {
        return request.flatMap(purchaseBookFacade::doPurchase)
                .collectList()
                .map(purchaseBookResponseList -> {
                    final List<ISBN> isbns = purchaseBookResponseList.stream()
                            .map(purchaseResponse -> purchaseResponse.purchasedIsbn())
                            .map(
                                    isbn -> ISBN.newBuilder()
                                            .setValue(isbn.isbn())
                                            .build()
                            ).toList();
                    return PurchaseBookResponse.newBuilder()
                            .addAllPurchasedBooks(isbns)
                            .build();
                });
    }
}
