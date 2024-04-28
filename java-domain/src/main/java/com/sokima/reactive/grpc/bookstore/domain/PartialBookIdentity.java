package com.sokima.reactive.grpc.bookstore.domain;

import org.apache.commons.lang3.StringUtils;
import org.immutables.value.Value;

@Value.Immutable
public interface PartialBookIdentity extends BookIdentity {
    @Value.Default
    default String description() {
        return StringUtils.EMPTY;
    }
}
