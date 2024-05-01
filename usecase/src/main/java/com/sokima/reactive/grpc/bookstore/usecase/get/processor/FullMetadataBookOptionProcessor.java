package com.sokima.reactive.grpc.bookstore.usecase.get.processor;

import com.sokima.reactive.grpc.bookstore.domain.helper.BookIdentificationOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.ChecksumSearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.FullMetadataSearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.get.processor.mapper.FullMetadata2ChecksumOptionMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FullMetadataBookOptionProcessor implements BookOptionProcessor<FullMetadataSearchOption> {
    private final BookOptionProcessor<ChecksumSearchOption> delegate;
    private final FullMetadata2ChecksumOptionMapper fullMetadata2ChecksumOptionMapper;

    public FullMetadataBookOptionProcessor(final BookOptionProcessor<ChecksumSearchOption> delegate,
                                           final FullMetadata2ChecksumOptionMapper fullMetadata2ChecksumOptionMapper) {
        this.delegate = delegate;
        this.fullMetadata2ChecksumOptionMapper = fullMetadata2ChecksumOptionMapper;
    }

    @Override
    public Flux<GetBookFlowResult> process(final FullMetadataSearchOption searchBookOption) {
        return Mono.just(searchBookOption)
                .map(fullMetadata2ChecksumOptionMapper::mapToChecksumOption)
                .flatMapMany(delegate::process);
    }

    @Override
    public boolean support(final String type) {
        return BookIdentificationOption.FULL_METADATA.name().equals(type);
    }
}
