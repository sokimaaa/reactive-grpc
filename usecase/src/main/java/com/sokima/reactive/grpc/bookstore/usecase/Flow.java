package com.sokima.reactive.grpc.bookstore.usecase;

import reactor.core.publisher.Flux;

public interface Flow<INBOUND, OUTBOUND> {
    Flux<OUTBOUND> doFlow(final INBOUND inbound);
}
