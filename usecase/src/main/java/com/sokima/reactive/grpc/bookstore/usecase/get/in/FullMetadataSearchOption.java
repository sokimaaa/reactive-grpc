package com.sokima.reactive.grpc.bookstore.usecase.get.in;

import com.sokima.reactive.grpc.bookstore.domain.helper.BookIdentificationOption;
import com.sokima.reactive.grpc.bookstore.domain.metadata.FullBookMetadata;
import org.immutables.value.Value;

@Value.Immutable
public interface FullMetadataSearchOption extends SearchOption<FullBookMetadata> {

    /**
     * @return the full book metadata value.
     */
    FullBookMetadata option();

    @Value.Default
    default String type() {
        return BookIdentificationOption.FULL_METADATA.name();
    }
}
