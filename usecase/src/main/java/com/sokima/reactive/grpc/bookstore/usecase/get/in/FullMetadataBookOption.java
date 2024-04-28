package com.sokima.reactive.grpc.bookstore.usecase.get.in;

import com.sokima.reactive.grpc.bookstore.domain.helper.OneofOptions;
import com.sokima.reactive.grpc.bookstore.domain.metadata.FullBookMetadata;
import org.immutables.value.Value;

@Value.Immutable
public interface FullMetadataBookOption extends SearchBookOption<FullBookMetadata> {

    /**
     * @return the full book metadata value.
     */
    FullBookMetadata option();

    @Value.Default
    default String type() {
        return OneofOptions.FULL_METADATA.name();
    }
}
