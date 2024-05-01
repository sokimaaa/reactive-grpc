package com.sokima.reactive.grpc.bookstore.usecase.get.processor;

import com.sokima.reactive.grpc.bookstore.domain.helper.Baggage;
import com.sokima.reactive.grpc.bookstore.domain.helper.BookIdentificationOption;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.ChecksumSearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.get.processor.mapper.Baggage2GetFlowResultMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ChecksumBookOptionProcessor implements BookOptionProcessor<ChecksumSearchOption> {

    private final FindBookPort findBookPort;
    private final Baggage2GetFlowResultMapper baggageMapper;

    public ChecksumBookOptionProcessor(final FindBookPort findBookPort,
                                       final Baggage2GetFlowResultMapper baggageMapper) {
        this.findBookPort = findBookPort;
        this.baggageMapper = baggageMapper;
    }

    @Override
    public Flux<GetBookFlowResult> process(final ChecksumSearchOption searchBookOption) {
        return Mono.zip(
                        findBookPort.findBookByChecksum(searchBookOption.option()),
                        findBookPort.findBookAggregationByChecksum(searchBookOption.option())
                )
                .map(tuple -> new Baggage<>(tuple.getT1(), tuple.getT2()))
                .map(baggageMapper::mapToGetBookFlowResult)
                .flux();
    }

    @Override
    public boolean support(final String type) {
        return BookIdentificationOption.CHECKSUM.name().equals(type);
    }
}
