package com.sokima.reactive.grpc.bookstore.usecase.supply;

import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.port.CreateBookPort;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.supply.in.SupplyBookInformation;
import com.sokima.reactive.grpc.bookstore.usecase.supply.mapper.Book2SupplyFlowResultMapper;
import com.sokima.reactive.grpc.bookstore.usecase.supply.out.SupplyBookFlowResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public final class SupplyBookFlow implements Flow<SupplyBookInformation, SupplyBookFlowResult> {

    private final FindBookPort findBookPort;
    private final CreateBookPort createBookPort;
    private final Book2SupplyFlowResultMapper bookMapper;

    public SupplyBookFlow(final FindBookPort findBookPort, final CreateBookPort createBookPort,
                          final Book2SupplyFlowResultMapper bookMapper) {
        this.findBookPort = findBookPort;
        this.createBookPort = createBookPort;
        this.bookMapper = bookMapper;
    }

    @Override
    public Flux<SupplyBookFlowResult> doFlow(final SupplyBookInformation supplyBookInformation) {
        return Mono.just(supplyBookInformation)
                .map(SupplyBookInformation::partialBookIdentity)
                .map(ChecksumGenerator::generateBookChecksum)
                .flatMap(findBookPort::findBookByChecksum)
                .flatMapMany(bookIdentity -> createBookPort.createBookN(bookIdentity, supplyBookInformation.supplyNumber()))
                .transform(Flux::collectList)
                .map(bookMapper::mapToSupplyFlowResult);
    }
}
