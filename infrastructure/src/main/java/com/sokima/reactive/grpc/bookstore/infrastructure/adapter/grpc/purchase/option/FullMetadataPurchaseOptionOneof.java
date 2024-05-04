package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.purchase.option;

import com.sokima.reactive.grpc.bookstore.domain.metadata.FullBookMetadata;
import com.sokima.reactive.grpc.bookstore.domain.metadata.ImmutableFullBookMetadata;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.ImmutableFullBookMetadataPurchaseOption;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.PurchaseOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FullMetadataPurchaseOptionOneof extends AbstractOneofResolver<PurchaseBookRequest, PurchaseOption<FullBookMetadata>>
        implements PurchaseOptionOneof<FullBookMetadata> {

    private static final Logger log = LoggerFactory.getLogger(FullMetadataPurchaseOptionOneof.class);

    @Override
    protected boolean condition(final PurchaseBookRequest oneof) {
        log.trace("Passed full metadata purchase option resolver with: {}", oneof);
        return oneof.hasFullBookMetadata();
    }

    @Override
    protected PurchaseOption<FullBookMetadata> result(final PurchaseBookRequest oneof) {
        log.debug("Oneof is resolved as Full Metadata Purchase Option: {}", oneof);
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
