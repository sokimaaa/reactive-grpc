package com.sokima.reactive.grpc.bookstore.domain.generator;

import com.sokima.reactive.grpc.bookstore.domain.BookIdentity;

import static java.lang.String.format;

public final class ChecksumGenerator extends SequenceGenerator {

    private ChecksumGenerator() {
        throw new UnsupportedOperationException("Instantiation of util class is forbidden.");
    }

    public static String generateBookChecksum(final BookIdentity identity) {
        return generateBookChecksum(identity.title(), identity.author(), identity.edition());
    }

    public static String generateBookChecksum(final String title, final String author, final String edition) {
        return generateChecksum(format("%s.%s.%s", title, author, edition));
    }
}
