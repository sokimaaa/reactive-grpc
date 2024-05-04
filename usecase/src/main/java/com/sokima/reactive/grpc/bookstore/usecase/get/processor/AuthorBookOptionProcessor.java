package com.sokima.reactive.grpc.bookstore.usecase.get.processor;

import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.helper.Baggage;
import com.sokima.reactive.grpc.bookstore.domain.helper.BookIdentificationOption;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.AuthorSearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.get.processor.mapper.Baggage2GetFlowResultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class AuthorBookOptionProcessor implements BookOptionProcessor<AuthorSearchOption> {

    private static final Logger log = LoggerFactory.getLogger(AuthorBookOptionProcessor.class);

    private final FindBookPort findBookPort;
    private final Baggage2GetFlowResultMapper baggageMapper;

    public AuthorBookOptionProcessor(final FindBookPort findBookPort,
                                     final Baggage2GetFlowResultMapper baggageMapper
    ) {
        this.findBookPort = findBookPort;
        this.baggageMapper = baggageMapper;
    }

    @Override
    public Flux<GetBookFlowResult> process(final AuthorSearchOption authorSearchOption) {
        log.debug("Processing author search option: {}", authorSearchOption);
        return findBookPort.findBooksByAuthor(authorSearchOption.option())
                .flatMap(bookIdentity -> {
                    final var checksum = ChecksumGenerator.generateBookChecksum(bookIdentity);
                    return findBookPort.findBookAggregationByChecksum(checksum)
                            .map(bookAggregation -> Baggage.of(bookIdentity, bookAggregation));
                })
                .map(baggageMapper::mapToGetBookFlowResult);
    }

    @Override
    public boolean support(final String type) {
        log.trace("Checking is author search option: {}", type);
        return BookIdentificationOption.AUTHOR.name().equals(type);
    }
}
