package com.sokima.reactive.grpc.bookstore.domain.generator;

public final class IsbnGenerator extends SequenceGenerator {
    private static final String ISBN_PATTERN = "%s-%s-%s-%s-%s";

    private IsbnGenerator() {
        throw new UnsupportedOperationException("Instantiation of util class is forbidden.");
    }

    public static String generateIsbn() {
        final String _13Digits = Long.toString(generate13SequenceNumber());
        return String.format(
                ISBN_PATTERN,
                _13Digits.substring(0, 3),
                _13Digits.substring(3, 5),
                _13Digits.substring(5, 10),
                _13Digits.substring(10, 12),
                _13Digits.charAt(12)
        );
    }
}
