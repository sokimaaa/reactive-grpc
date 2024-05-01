package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc;

import com.google.protobuf.Message;
import reactor.core.publisher.Flux;

public interface Workflow<INBOUND extends Message, OUTBOUND extends Message> {
    Flux<OUTBOUND> doWorkflow(final INBOUND inbound);
}
