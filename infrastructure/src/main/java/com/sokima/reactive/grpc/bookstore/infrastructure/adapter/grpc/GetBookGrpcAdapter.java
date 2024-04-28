package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc;

import com.sokima.reactive.grpc.bookstore.proto.Checksum;
import com.sokima.reactive.grpc.bookstore.proto.GetBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.GetBookResponse;
import com.sokima.reactive.grpc.bookstore.proto.ReactorGetBookUseCaseGrpc;
import com.sokima.reactive.grpc.bookstore.usecase.get.GetBookFacade;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@GrpcService
public class GetBookGrpcAdapter extends ReactorGetBookUseCaseGrpc.GetBookUseCaseImplBase {

    private final GetBookFacade getBookFacade;

    public GetBookGrpcAdapter(final GetBookFacade getBookFacade) {
        this.getBookFacade = getBookFacade;
    }

    @Override
    public Flux<GetBookResponse> getBooks(final Mono<GetBookRequest> request) {
        return request.flatMapMany(getBookFacade::doGet)
                .map(getBookResponse -> GetBookResponse.newBuilder()
                        .setChecksum(
                                Checksum.newBuilder()
                                        .setValue(getBookResponse.checksum())
                                        .build()
                        )
                        .setAuthor(getBookResponse.author())
                        .setTitle(getBookResponse.title())
                        .setEdition(getBookResponse.edition())
                        .setDescription(getBookResponse.description())
                        .setIsAvailable(getBookResponse.isAvailable())
                        .build());
    }
}
