package com.sokima.reactive.grpc.bookstore.usecase.get.processor;

import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.helper.Baggage;
import com.sokima.reactive.grpc.bookstore.domain.helper.BookIdentificationOption;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.TitleSearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.get.processor.mapper.Baggage2GetFlowResultMapper;
import reactor.core.publisher.Flux;

public class TitleBookOptionProcessor implements BookOptionProcessor<TitleSearchOption> {

    private final FindBookPort findBookPort;
    private final Baggage2GetFlowResultMapper baggageMapper;

    public TitleBookOptionProcessor(final FindBookPort findBookPort,
                                    final Baggage2GetFlowResultMapper baggageMapper) {
        this.findBookPort = findBookPort;
        this.baggageMapper = baggageMapper;
    }

    @Override
    public Flux<GetBookFlowResult> process(final TitleSearchOption searchBookOption) {
        return findBookPort.findBooksByTitle(searchBookOption.option())
                .flatMap(bookIdentity -> {
                    final var checksum = ChecksumGenerator.generateBookChecksum(bookIdentity);
                    return findBookPort.findBookAggregationByChecksum(checksum)
                            .map(bookAggregation -> Baggage.of(bookIdentity, bookAggregation));
                })
                .map(baggageMapper::mapToGetBookFlowResult);
    }

    @Override
    public boolean support(final String type) {
        return BookIdentificationOption.TITLE.name().equals(type);
    }
}
