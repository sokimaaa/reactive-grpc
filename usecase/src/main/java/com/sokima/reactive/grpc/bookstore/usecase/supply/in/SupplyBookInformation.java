package com.sokima.reactive.grpc.bookstore.usecase.supply.in;

import com.sokima.reactive.grpc.bookstore.domain.PartialBookIdentity;
import org.immutables.value.Value;

@Value.Immutable
public interface SupplyBookInformation {
    PartialBookIdentity partialBookIdentity();
    Integer supplyNumber();
}
