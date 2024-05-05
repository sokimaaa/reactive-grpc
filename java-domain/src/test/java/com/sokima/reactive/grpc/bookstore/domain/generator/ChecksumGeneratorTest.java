package com.sokima.reactive.grpc.bookstore.domain.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

class ChecksumGeneratorTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/checksum-generator.csv", numLinesToSkip = 1)
    void testGenerateBookChecksum(final String title, final String author, final String edition, final String expectedChecksum) {
        final String actual = ChecksumGenerator.generateBookChecksum(title, author, edition);
        Assertions.assertEquals(expectedChecksum, actual);
    }
}
