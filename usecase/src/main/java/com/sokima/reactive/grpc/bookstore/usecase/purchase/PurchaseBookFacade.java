package com.sokima.reactive.grpc.bookstore.usecase.purchase;

import com.sokima.reactive.grpc.bookstore.domain.oneof.OneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.PurchaseOption;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.out.PurchaseBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.processor.ErrorPurchaseOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.processor.PurchaseOptionProcessor;
import reactor.core.publisher.Flux;

import java.util.List;

public final class PurchaseBookFacade {

    private final List<PurchaseOptionProcessor<?>> processors;

    private final ErrorPurchaseOptionProcessor fallbackProcessor;

    private final OneofResolver<PurchaseBookRequest, PurchaseOption<?>> resolver;

    public PurchaseBookFacade(final List<PurchaseOptionProcessor<?>> processors,
                              final ErrorPurchaseOptionProcessor fallbackProcessor,
                              final OneofResolver<PurchaseBookRequest, PurchaseOption<?>> resolver) {
        this.processors = processors;
        this.fallbackProcessor = fallbackProcessor;
        this.resolver = resolver;
    }

    public Flux<PurchaseBookResponse> doPurchase(final PurchaseBookRequest purchaseBookRequest) {
        final PurchaseOption<?> option = resolver.resolve(purchaseBookRequest);
        final PurchaseOptionProcessor processor = processors.stream()
                .filter(proc -> proc.support(option.type()))
                .findFirst()
                .orElse(fallbackProcessor);
        return processor.process(option);
    }
}
