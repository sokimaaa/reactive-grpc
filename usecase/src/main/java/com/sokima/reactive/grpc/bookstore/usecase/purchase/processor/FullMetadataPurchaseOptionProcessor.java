package com.sokima.reactive.grpc.bookstore.usecase.purchase.processor;

import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.helper.BookIdentificationOption;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.domain.port.UpdateBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.FullBookMetadataPurchaseOption;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.out.PurchaseBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.processor.mapper.Container2PurchaseFlowResultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class FullMetadataPurchaseOptionProcessor implements PurchaseOptionProcessor<FullBookMetadataPurchaseOption> {

    private static final Logger log = LoggerFactory.getLogger(FullMetadataPurchaseOptionProcessor.class);

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
        log.debug("Processing full metadata purchase option: {}", purchaseOption);
        final var option = purchaseOption.option();
        final var checksum = ChecksumGenerator.generateBookChecksum(option.title(), option.author(), option.edition());
        return findBookPort.nextBookByChecksum(checksum)
                .flatMap(book -> updateBookPort.updateBookIsPurchasedField(book.isbn(), Boolean.TRUE))
                .filter(UpdateBookPort.Container::isUpdated)
                .flatMap(bookContainer -> findBookPort.findBookAggregationByChecksum(checksum)
                        .flatMap(bookAggregation -> updateBookPort.updateBookAggregationQuantity(
                                        checksum, bookAggregation.quantity() - 1
                                )
                        ).thenReturn(bookContainer)
                )
                .map(containerMapper::mapToPurchaseBookFlowResult)
                .flux();
    }

    @Override
    public boolean support(final String type) {
        log.trace("Checking is full metadata purchase option: {}", type);
        return BookIdentificationOption.FULL_METADATA.name().equals(type);
    }
}
