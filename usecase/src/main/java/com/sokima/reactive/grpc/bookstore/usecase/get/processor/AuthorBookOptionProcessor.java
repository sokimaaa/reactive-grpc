package com.sokima.reactive.grpc.bookstore.usecase.get.processor;

import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.helper.Baggage;
import com.sokima.reactive.grpc.bookstore.domain.helper.BookIdentificationOption;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.AuthorSearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.get.processor.mapper.Baggage2GetFlowResultMapper;
import reactor.core.publisher.Flux;

public class AuthorBookOptionProcessor implements BookOptionProcessor<AuthorSearchOption> {
    private final FindBookPort findBookPort;
    private final Baggage2GetFlowResultMapper baggageMapper;

    public AuthorBookOptionProcessor(final FindBookPort findBookPort,
                                     final Baggage2GetFlowResultMapper baggageMapper
    ) {
        this.findBookPort = findBookPort;
        this.baggageMapper = baggageMapper;
    }

    @Override
    public Flux<GetBookFlowResult> process(final AuthorSearchOption searchBookOption) {
        return findBookPort.findBooksByAuthor(searchBookOption.option())
                .flatMap(bookIdentity -> {
                    final var checksum = ChecksumGenerator.generateBookChecksum(bookIdentity);
                    return findBookPort.findBookAggregationByChecksum(checksum)
                            .map(bookAggregation -> Baggage.of(bookIdentity, bookAggregation));
                })
                .map(baggageMapper::mapToGetBookFlowResult);
    }

    @Override
    public boolean support(final String type) {
        return BookIdentificationOption.AUTHOR.name().equals(type);
    }
}
