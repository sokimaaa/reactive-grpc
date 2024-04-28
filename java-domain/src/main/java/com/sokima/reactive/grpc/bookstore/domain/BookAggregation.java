package com.sokima.reactive.grpc.bookstore.domain;

import org.immutables.value.Value;

@Value.Immutable
public interface BookAggregation {
    /**
     * A unique identifier of book in the system.
     * Despite {@link Book#isbn()} not necessary to uniquely identify a physical book.
     * <p/>
     * Business value of checksum is to track quantity of the similar book in the storage.
     * <p/>
     * Checksum is a hash generated based on {@link BookIdentity#title()}, {@link BookIdentity#author()},
     * and {@link BookIdentity#edition()} values.
     *
     * @return the checksum value.
     */
    String checksum();

    /**
     * The number of books of {@link BookIdentity} in the stock.
     *
     * @return the quantity value.
     */
    Long quantity();
}
