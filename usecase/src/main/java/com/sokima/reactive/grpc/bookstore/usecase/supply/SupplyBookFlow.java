package com.sokima.reactive.grpc.bookstore.usecase.supply;

import com.sokima.reactive.grpc.bookstore.domain.Book;
import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.port.CreateBookPort;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.supply.in.SupplyBookInformation;
import com.sokima.reactive.grpc.bookstore.usecase.supply.out.ImmutableSupplyBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.supply.out.SupplyBookFlowResult;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public final class SupplyBookFlow implements Flow<SupplyBookInformation, SupplyBookFlowResult> {

    private final FindBookPort findBookPort;
    private final CreateBookPort createBookPort;

    public SupplyBookFlow(final FindBookPort findBookPort, final CreateBookPort createBookPort) {
        this.findBookPort = findBookPort;
        this.createBookPort = createBookPort;
    }

    @Override
    public Flux<SupplyBookFlowResult> doFlow(final SupplyBookInformation supplyBookInformation) {
        return Mono.just(supplyBookInformation)
                .map(SupplyBookInformation::partialBookIdentity)
                .map(ChecksumGenerator::generateBookChecksum)
                .flatMap(findBookPort::findBookByChecksum)
                .flatMapMany(bookIdentity -> createBookPort.createBookN(bookIdentity, supplyBookInformation.supplyNumber()))
                .transform(Flux::collectList)
                .map(supplied -> ImmutableSupplyBookFlowResult.builder()
                        .isbns(supplied.stream().map(Book::isbn).toList())
                        .checksum(supplied.stream().findFirst()
                                .map(Book::bookIdentity)
                                .map(ChecksumGenerator::generateBookChecksum)
                                .orElse(StringUtils.EMPTY))
                        .build());
    }
}
