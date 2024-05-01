package com.sokima.reactive.grpc.bookstore.usecase.get.processor;

import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.helper.BookIdentificationOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.ChecksumSearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.FullMetadataSearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.ImmutableChecksumSearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookFlowResult;
import reactor.core.publisher.Flux;

public class FullMetadataBookOptionProcessor implements BookOptionProcessor<FullMetadataSearchOption> {
    private final BookOptionProcessor<ChecksumSearchOption> delegate;

    public FullMetadataBookOptionProcessor(final BookOptionProcessor<ChecksumSearchOption> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Flux<GetBookFlowResult> process(final FullMetadataSearchOption searchBookOption) {
        final var option = searchBookOption.option();
        final var checksum = ChecksumGenerator.generateBookChecksum(option.title(), option.author(), option.edition());
        final var checksumSearchOption = ImmutableChecksumSearchOption.builder()
                .option(checksum)
                .build();
        return delegate.process(checksumSearchOption);
    }

    @Override
    public boolean support(final String type) {
        return BookIdentificationOption.FULL_METADATA.name().equals(type);
    }
}
