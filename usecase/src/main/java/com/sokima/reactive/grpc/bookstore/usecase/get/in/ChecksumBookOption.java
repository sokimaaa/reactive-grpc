package com.sokima.reactive.grpc.bookstore.usecase.get.in;

import com.sokima.reactive.grpc.bookstore.domain.helper.OneofOptions;
import org.immutables.value.Value;

@Value.Immutable
public interface ChecksumBookOption extends SearchBookOption<String> {
    /**
     * @return the checksum value.
     */
    String option();

    @Value.Default
    default String type() {
        return OneofOptions.CHECKSUM.name();
    }
}
