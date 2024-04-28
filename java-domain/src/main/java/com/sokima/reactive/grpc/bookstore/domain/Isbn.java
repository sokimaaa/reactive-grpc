package com.sokima.reactive.grpc.bookstore.domain;

import org.immutables.value.Value;

@Value.Immutable
public interface Isbn {
    /**
     * A unique numeric commercial book identifier
     * that is intended to be used by publishers, booksellers,
     * libraries, and other institutions in the book industry
     * to identify a specific edition of a book.
     * <p/>
     * Each physical book has unique ISBN. Even if it is a copy.
     * <p/>
     * Isbn is the 13 digits value with pattern: XXX-XX-XXXXX-XX-X.
     *
     * @return the isbn value.
     */
    String isbn();
}
