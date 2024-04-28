package com.sokima.reactive.grpc.bookstore.usecase.supply;

import com.sokima.reactive.grpc.bookstore.domain.Book;
import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.port.CreateBookPort;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.proto.SupplyBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.supply.out.ImmutableSupplyBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.supply.out.SupplyBookResponse;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

public final class SupplyBookFacade {

    private final FindBookPort findBookPort;
    private final CreateBookPort createBookPort;

    public SupplyBookFacade(final FindBookPort findBookPort,
                            final CreateBookPort createBookPort) {
        this.findBookPort = findBookPort;
        this.createBookPort = createBookPort;
    }

    public Mono<SupplyBookResponse> doSupply(final SupplyBookRequest supplyBookRequest) {
        final String checksum = ChecksumGenerator.generateBookChecksum(
                supplyBookRequest.getTitle(),
                supplyBookRequest.getAuthor(),
                supplyBookRequest.getEdition()
        );
        return findBookPort.findBookByChecksum(checksum)
                .flatMapMany(bookIdentity ->
                        createBookPort.createBookN(bookIdentity, supplyBookRequest.getSupplyNumber())
                )
                .collectList()
                .map(supplied -> ImmutableSupplyBookResponse.builder()
                        .isbns(supplied.stream().map(Book::isbn).toList())
                        .checksum(supplied.stream().findFirst()
                                .map(Book::bookIdentity)
                                .map(ChecksumGenerator::generateBookChecksum)
                                .orElse(StringUtils.EMPTY))
                        .build());
    }
}
