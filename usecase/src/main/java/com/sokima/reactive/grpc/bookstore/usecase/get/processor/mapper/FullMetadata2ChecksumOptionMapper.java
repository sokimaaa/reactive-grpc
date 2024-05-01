package com.sokima.reactive.grpc.bookstore.usecase.get.processor.mapper;

import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.ChecksumSearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.FullMetadataSearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.ImmutableChecksumSearchOption;

public class FullMetadata2ChecksumOptionMapper {
    public ChecksumSearchOption mapToChecksumOption(final FullMetadataSearchOption fullMetadataSearchOption) {
        final var option = fullMetadataSearchOption.option();
        final var checksum = ChecksumGenerator.generateBookChecksum(option.title(), option.author(), option.edition());
        return ImmutableChecksumSearchOption.builder()
                .option(checksum)
                .build();
    }
}
