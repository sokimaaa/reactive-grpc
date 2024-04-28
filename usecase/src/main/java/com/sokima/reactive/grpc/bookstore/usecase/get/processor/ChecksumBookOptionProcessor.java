package com.sokima.reactive.grpc.bookstore.usecase.get.processor;

import com.sokima.reactive.grpc.bookstore.domain.helper.OneofOptions;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.ChecksumBookOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.ImmutableGetBookResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ChecksumBookOptionProcessor implements BookOptionProcessor<ChecksumBookOption> {
    private final FindBookPort findBookPort;

    public ChecksumBookOptionProcessor(final FindBookPort findBookPort) {
        this.findBookPort = findBookPort;
    }

    @Override
    public Flux<GetBookResponse> process(final ChecksumBookOption searchBookOption) {
        return Mono.zip(
                        findBookPort.findBookByChecksum(searchBookOption.option()),
                        findBookPort.findBookAggregationByChecksum(searchBookOption.option())
                ).map(tuple -> (GetBookResponse) ImmutableGetBookResponse.builder()
                        .checksum(searchBookOption.option())
                        .title(tuple.getT1().title())
                        .author(tuple.getT1().author())
                        .edition(tuple.getT1().edition())
                        .description(tuple.getT1().description())
                        .isAvailable(tuple.getT2().quantity() > 0)
                        .build()
                )
                .flux();
    }

    @Override
    public boolean support(final String type) {
        return OneofOptions.CHECKSUM.name().equals(type);
    }
}
