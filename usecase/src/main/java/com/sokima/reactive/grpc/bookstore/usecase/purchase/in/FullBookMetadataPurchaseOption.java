package com.sokima.reactive.grpc.bookstore.usecase.purchase.in;

import com.sokima.reactive.grpc.bookstore.domain.helper.BookIdentificationOption;
import com.sokima.reactive.grpc.bookstore.domain.metadata.FullBookMetadata;
import org.immutables.value.Value;

@Value.Immutable
public interface FullBookMetadataPurchaseOption extends PurchaseOption<FullBookMetadata> {
    @Value.Default
    default String type() {
        return BookIdentificationOption.FULL_METADATA.name();
    }
}
