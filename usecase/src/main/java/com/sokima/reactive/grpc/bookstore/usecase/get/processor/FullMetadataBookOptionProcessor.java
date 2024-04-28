package com.sokima.reactive.grpc.bookstore.usecase.get.processor;

import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.helper.OneofOptions;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.ChecksumBookOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.FullMetadataBookOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.ImmutableChecksumBookOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookResponse;
import reactor.core.publisher.Flux;

public class FullMetadataBookOptionProcessor implements BookOptionProcessor<FullMetadataBookOption> {
    private final BookOptionProcessor<ChecksumBookOption> delegate;

    public FullMetadataBookOptionProcessor(final BookOptionProcessor<ChecksumBookOption> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Flux<GetBookResponse> process(final FullMetadataBookOption searchBookOption) {
        final var option = searchBookOption.option();
        final var checksum = ChecksumGenerator.generateBookChecksum(option.title(), option.author(), option.edition());
        final var checksumSearchOption = ImmutableChecksumBookOption.builder()
                .option(checksum)
                .build();
        return delegate.process(checksumSearchOption);
    }

    @Override
    public boolean support(final String type) {
        return OneofOptions.FULL_METADATA.name().equals(type);
    }
}
