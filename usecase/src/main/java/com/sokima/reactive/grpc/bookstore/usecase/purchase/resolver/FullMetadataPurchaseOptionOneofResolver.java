package com.sokima.reactive.grpc.bookstore.usecase.purchase.resolver;

import com.sokima.reactive.grpc.bookstore.domain.metadata.ImmutableFullBookMetadata;
import com.sokima.reactive.grpc.bookstore.domain.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.ImmutableFullBookMetadataPurchaseOption;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.PurchaseOption;

public class FullMetadataPurchaseOptionOneofResolver extends AbstractOneofResolver<PurchaseBookRequest, PurchaseOption<?>>
        implements PurchaseOptionOneofResolver {
    @Override
    public PurchaseOption<?> resolve(final PurchaseBookRequest oneof) {
        if (!oneof.hasFullBookMetadata()) {
            return next().resolve(oneof);
        }
        final var fullBookMetadata = oneof.getFullBookMetadata();
        return ImmutableFullBookMetadataPurchaseOption.builder()
                .option(ImmutableFullBookMetadata.builder()
                        .author(fullBookMetadata.getAuthor())
                        .title(fullBookMetadata.getTitle())
                        .edition(fullBookMetadata.getEdition())
                        .build())
                .build();
    }
}
