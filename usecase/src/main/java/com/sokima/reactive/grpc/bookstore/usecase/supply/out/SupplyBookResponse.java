package com.sokima.reactive.grpc.bookstore.usecase.supply.out;

import com.sokima.reactive.grpc.bookstore.domain.Isbn;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface SupplyBookResponse {

    String checksum();

    List<Isbn> isbns();
}
