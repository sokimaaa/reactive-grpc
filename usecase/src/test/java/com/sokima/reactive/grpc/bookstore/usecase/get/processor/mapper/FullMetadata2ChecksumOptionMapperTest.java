package com.sokima.reactive.grpc.bookstore.usecase.get.processor.mapper;

import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.metadata.ImmutableFullBookMetadata;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.ImmutableFullMetadataSearchOption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FullMetadata2ChecksumOptionMapperTest {

    @InjectMocks
    FullMetadata2ChecksumOptionMapper mapper;

    @Test
    void mapToChecksumOption_validInput_validOutput() {
        final String title = "title";
        final String author = "author";
        final String edition = "edition";
        final String checksum = ChecksumGenerator.generateBookChecksum(title, author, edition);
        final var input = ImmutableFullMetadataSearchOption.builder()
                .option(ImmutableFullBookMetadata.builder()
                        .author(author)
                        .edition(edition)
                        .title(title)
                        .build())
                .type("type")
                .build();

        final var actual = mapper.mapToChecksumOption(input);

        Assertions.assertEquals(checksum, actual.option());
        Assertions.assertEquals("CHECKSUM", actual.type());
    }
}
