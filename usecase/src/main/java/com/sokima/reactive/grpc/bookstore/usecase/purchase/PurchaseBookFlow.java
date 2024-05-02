package com.sokima.reactive.grpc.bookstore.usecase.purchase;

import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.PurchaseOption;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.out.PurchaseBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.processor.ErrorPurchaseOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.processor.PurchaseOptionProcessor;
import reactor.core.publisher.Flux;

import java.util.List;

public final class PurchaseBookFlow implements Flow<PurchaseOption<?>, List<PurchaseBookFlowResult>> {
    private final List<PurchaseOptionProcessor<? extends PurchaseOption<?>>> processors;
    private final ErrorPurchaseOptionProcessor<PurchaseOption<?>> fallbackProcessor;

    public PurchaseBookFlow(
            final List<PurchaseOptionProcessor<? extends PurchaseOption<?>>> processors,
            final ErrorPurchaseOptionProcessor<PurchaseOption<?>> fallbackProcessor) {
        this.processors = processors;
        this.fallbackProcessor = fallbackProcessor;
    }

    @Override
    public Flux<List<PurchaseBookFlowResult>> doFlow(final PurchaseOption<?> purchaseOption) {
        return processors.stream()
                .filter(processor -> processor.support(purchaseOption.type()))
                .findFirst()
                .orElse(fallbackProcessor)
                .safeCastAndProcess(purchaseOption)
                .transform(Flux::collectList);
    }
}
