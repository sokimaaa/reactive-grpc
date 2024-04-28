package com.sokima.reactive.grpc.bookstore.usecase.purchase.resolver;

import com.sokima.reactive.grpc.bookstore.domain.metadata.ImmutableFullBookMetadata;
import com.sokima.reactive.grpc.bookstore.domain.oneof.OneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.ImmutableFullBookMetadataPurchaseOption;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.PurchaseOption;

public class FullMetadataPurchaseOptionOneofResolver implements PurchaseOptionOneofResolver {
    private final PurchaseOptionOneofResolver next;

    public FullMetadataPurchaseOptionOneofResolver(final PurchaseOptionOneofResolver next) {
        this.next = next;
    }

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

    @Override
    public OneofResolver<PurchaseBookRequest, PurchaseOption<?>> next() {
        return next;
    }
}
