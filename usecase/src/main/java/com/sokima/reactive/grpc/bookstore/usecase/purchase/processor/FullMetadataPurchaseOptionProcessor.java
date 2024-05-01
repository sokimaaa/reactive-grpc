package com.sokima.reactive.grpc.bookstore.usecase.purchase.processor;

import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.helper.BookIdentificationOption;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.domain.port.UpdateBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.FullBookMetadataPurchaseOption;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.out.PurchaseBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.processor.mapper.Container2PurchaseFlowResultMapper;
import reactor.core.publisher.Flux;

public class FullMetadataPurchaseOptionProcessor implements PurchaseOptionProcessor<FullBookMetadataPurchaseOption> {

    private final FindBookPort findBookPort;
    private final UpdateBookPort updateBookPort;
    private final Container2PurchaseFlowResultMapper containerMapper;

    public FullMetadataPurchaseOptionProcessor(final FindBookPort findBookPort, final UpdateBookPort updateBookPort,
                                               final Container2PurchaseFlowResultMapper containerMapper) {
        this.findBookPort = findBookPort;
        this.updateBookPort = updateBookPort;
        this.containerMapper = containerMapper;
    }

    @Override
    public Flux<PurchaseBookFlowResult> process(final FullBookMetadataPurchaseOption purchaseOption) {
        final var option = purchaseOption.option();
        final var checksum = ChecksumGenerator.generateBookChecksum(option.title(), option.author(), option.edition());
        return findBookPort.nextBookByChecksum(checksum)
                .flatMap(book -> updateBookPort.updateBookIsPurchasedField(book.isbn(), Boolean.TRUE))
                .filter(UpdateBookPort.Container::isUpdated)
                .map(containerMapper::mapToPurchaseBookFlowResult)
                .flux();
    }

    @Override
    public boolean support(final String type) {
        return BookIdentificationOption.FULL_METADATA.name().equals(type);
    }
}
