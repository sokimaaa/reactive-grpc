package com.sokima.reactive.grpc.bookstore.usecase.get.processor;

import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.helper.Baggage;
import com.sokima.reactive.grpc.bookstore.domain.helper.BookIdentificationOption;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.TitleSearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.get.processor.mapper.Baggage2GetFlowResultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class TitleSearchOptionProcessor implements BookOptionProcessor<TitleSearchOption> {

    private static final Logger log = LoggerFactory.getLogger(TitleSearchOptionProcessor.class);

    private final FindBookPort findBookPort;
    private final Baggage2GetFlowResultMapper baggageMapper;

    public TitleSearchOptionProcessor(final FindBookPort findBookPort,
                                      final Baggage2GetFlowResultMapper baggageMapper) {
        this.findBookPort = findBookPort;
        this.baggageMapper = baggageMapper;
    }

    @Override
    public Flux<GetBookFlowResult> process(final TitleSearchOption titleSearchOption) {
        log.debug("Processing title search option: {}", titleSearchOption);
        return findBookPort.findBooksByTitle(titleSearchOption.option())
                .flatMap(bookIdentity -> {
                    final var checksum = ChecksumGenerator.generateBookChecksum(bookIdentity);
                    return findBookPort.findBookAggregationByChecksum(checksum)
                            .map(bookAggregation -> Baggage.of(bookIdentity, bookAggregation));
                })
                .map(baggageMapper::mapToGetBookFlowResult);
    }

    @Override
    public boolean support(final String type) {
        log.trace("Checking is title search option: {}", type);
        return BookIdentificationOption.TITLE.name().equalsIgnoreCase(type);
    }
}
