package com.sokima.reactive.grpc.bookstore.usecase.get.processor;

import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.helper.Baggage;
import com.sokima.reactive.grpc.bookstore.domain.helper.BookIdentificationOption;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.AuthorSearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.ImmutableGetBookFlowResult;
import reactor.core.publisher.Flux;

public class AuthorBookOptionProcessor implements BookOptionProcessor<AuthorSearchOption> {
    private static final int MIN_AVAILABLE_QUANTITY = 0;
    private final FindBookPort findBookPort;

    public AuthorBookOptionProcessor(final FindBookPort findBookPort) {
        this.findBookPort = findBookPort;
    }

    @Override
    public Flux<GetBookFlowResult> process(final AuthorSearchOption searchBookOption) {
        return findBookPort.findBooksByAuthor(searchBookOption.option())
                .flatMap(bookIdentity -> {
                    final var checksum = ChecksumGenerator.generateBookChecksum(bookIdentity);
                    return findBookPort.findBookAggregationByChecksum(checksum)
                            .map(bookAggregation -> Baggage.of(bookIdentity, bookAggregation));
                })
                .map(baggage -> ImmutableGetBookFlowResult.builder()
                        .checksum(baggage.value().checksum())
                        .author(baggage.bookIdentity().author())
                        .title(baggage.bookIdentity().title())
                        .edition(baggage.bookIdentity().edition())
                        .description(baggage.bookIdentity().description())
                        .isAvailable(baggage.value().quantity() > MIN_AVAILABLE_QUANTITY)
                        .build());
    }

    @Override
    public boolean support(final String type) {
        return BookIdentificationOption.AUTHOR.name().equals(type);
    }
}
