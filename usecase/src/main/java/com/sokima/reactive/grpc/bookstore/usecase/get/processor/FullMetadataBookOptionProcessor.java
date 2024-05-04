package com.sokima.reactive.grpc.bookstore.usecase.get.processor;

import com.sokima.reactive.grpc.bookstore.domain.helper.BookIdentificationOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.ChecksumSearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.FullMetadataSearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.get.processor.mapper.FullMetadata2ChecksumOptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FullMetadataBookOptionProcessor implements BookOptionProcessor<FullMetadataSearchOption> {

    private static final Logger log = LoggerFactory.getLogger(FullMetadataBookOptionProcessor.class);

    private final BookOptionProcessor<ChecksumSearchOption> delegate;
    private final FullMetadata2ChecksumOptionMapper fullMetadata2ChecksumOptionMapper;

    public FullMetadataBookOptionProcessor(final BookOptionProcessor<ChecksumSearchOption> delegate,
                                           final FullMetadata2ChecksumOptionMapper fullMetadata2ChecksumOptionMapper) {
        this.delegate = delegate;
        this.fullMetadata2ChecksumOptionMapper = fullMetadata2ChecksumOptionMapper;
    }

    @Override
    public Flux<GetBookFlowResult> process(final FullMetadataSearchOption fullMetadataSearchOption) {
        log.debug("Processing full metadata search option: {}", fullMetadataSearchOption);
        return Mono.just(fullMetadataSearchOption)
                .map(fullMetadata2ChecksumOptionMapper::mapToChecksumOption)
                .flatMapMany(delegate::process);
    }

    @Override
    public boolean support(final String type) {
        log.trace("Checking is full metadata search option: {}", type);
        return BookIdentificationOption.FULL_METADATA.name().equals(type);
    }
}
