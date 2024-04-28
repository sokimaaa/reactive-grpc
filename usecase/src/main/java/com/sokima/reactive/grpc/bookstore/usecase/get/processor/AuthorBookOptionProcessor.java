package com.sokima.reactive.grpc.bookstore.usecase.get.processor;

import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.helper.Baggage;
import com.sokima.reactive.grpc.bookstore.domain.helper.OneofOptions;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.AuthorBookOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.ImmutableGetBookResponse;
import reactor.core.publisher.Flux;

public class AuthorBookOptionProcessor implements BookOptionProcessor<AuthorBookOption> {
    private final FindBookPort findBookPort;

    public AuthorBookOptionProcessor(final FindBookPort findBookPort) {
        this.findBookPort = findBookPort;
    }

    @Override
    public Flux<GetBookResponse> process(final AuthorBookOption searchBookOption) {
        return findBookPort.findBooksByAuthor(searchBookOption.option())
                .flatMap(bookIdentity -> {
                    final var checksum = ChecksumGenerator.generateBookChecksum(bookIdentity);
                    return findBookPort.findBookAggregationByChecksum(checksum)
                            .map(bookAggregation -> Baggage.of(bookIdentity, bookAggregation));
                })
                .map(baggage -> ImmutableGetBookResponse.builder()
                        .checksum(baggage.value().checksum())
                        .author(baggage.bookIdentity().author())
                        .title(baggage.bookIdentity().title())
                        .edition(baggage.bookIdentity().edition())
                        .description(baggage.bookIdentity().description())
                        .isAvailable(baggage.value().quantity() > 0)
                        .build());
    }

    @Override
    public boolean support(final String type) {
        return OneofOptions.AUTHOR.name().equals(type);
    }
}
