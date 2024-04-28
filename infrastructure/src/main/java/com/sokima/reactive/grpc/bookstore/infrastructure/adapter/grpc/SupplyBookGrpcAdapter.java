package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc;

import com.sokima.reactive.grpc.bookstore.domain.Isbn;
import com.sokima.reactive.grpc.bookstore.proto.*;
import com.sokima.reactive.grpc.bookstore.usecase.supply.SupplyBookFacade;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Flux;

@GrpcService
public class SupplyBookGrpcAdapter extends ReactorSupplyBookUseCaseGrpc.SupplyBookUseCaseImplBase {

    private final SupplyBookFacade supplyBookFacade;

    public SupplyBookGrpcAdapter(final SupplyBookFacade supplyBookFacade) {
        this.supplyBookFacade = supplyBookFacade;
    }

    @Override
    public Flux<SupplyBookResponse> supplyBooks(final Flux<SupplyBookRequest> requestFlux) {
        return requestFlux.flatMap(supplyBookFacade::doSupply)
                .map(response -> SupplyBookResponse.newBuilder()
                        .addAllIsbn(response.isbns().stream()
                                .map(Isbn::isbn)
                                .map(isbn -> ISBN.newBuilder().setValue(isbn).build())
                                .toList()
                        )
                        .setChecksum(Checksum.newBuilder().setValue(response.checksum()).build())
                        .setQuantity(response.isbns().size())
                        .build()
                );
    }
}
