package com.sokima.reactive.grpc.bookstore.usecase.purchase.processor;

import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.helper.OneofOptions;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.domain.port.UpdateBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.FullBookMetadataPurchaseOption;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.out.ImmutablePurchaseBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.out.PurchaseBookFlowResult;
import reactor.core.publisher.Flux;

public class FullMetadataPurchaseOptionProcessor implements PurchaseOptionProcessor<FullBookMetadataPurchaseOption> {

    private final FindBookPort findBookPort;
    private final UpdateBookPort updateBookPort;

    public FullMetadataPurchaseOptionProcessor(final FindBookPort findBookPort, final UpdateBookPort updateBookPort) {
        this.findBookPort = findBookPort;
        this.updateBookPort = updateBookPort;
    }

    @Override
    public Flux<PurchaseBookFlowResult> process(final FullBookMetadataPurchaseOption purchaseOption) {
        final var option = purchaseOption.option();
        final var checksum = ChecksumGenerator.generateBookChecksum(option.title(), option.author(), option.edition());
        return findBookPort.nextBookByChecksum(checksum)
                .flatMap(book -> updateBookPort.updateBookIsPurchasedField(book.isbn(), Boolean.TRUE)
                )
                .filter(UpdateBookPort.Container::isUpdated)
                .<PurchaseBookFlowResult>map(containerOfBook -> ImmutablePurchaseBookFlowResult.builder()
                        .purchasedIsbn(containerOfBook.newDomainObject().isbn())
                        .build())
                .flux();
    }

    @Override
    public boolean support(final String type) {
        return OneofOptions.FULL_METADATA.name().equals(type);
    }
}
