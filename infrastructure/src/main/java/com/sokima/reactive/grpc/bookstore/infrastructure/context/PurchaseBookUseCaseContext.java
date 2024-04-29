package com.sokima.reactive.grpc.bookstore.infrastructure.context;

import com.sokima.reactive.grpc.bookstore.domain.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.domain.oneof.OneofResolver;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.domain.port.UpdateBookPort;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.PurchaseBookFacade;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.FullBookMetadataPurchaseOption;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.PurchaseOption;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.processor.ErrorPurchaseOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.processor.FullMetadataPurchaseOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.processor.PurchaseOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.resolver.FullMetadataPurchaseOptionOneofResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class PurchaseBookUseCaseContext {
    @Bean
    PurchaseBookFacade purchaseBookFacade(final List<PurchaseOptionProcessor<?>> purchaseOptionProcessors,
                                          final ErrorPurchaseOptionProcessor errorPurchaseOptionProcessor,
                                          final OneofResolver<PurchaseBookRequest, PurchaseOption<?>> purchaseOptionOneofResolver) {
        return new PurchaseBookFacade(purchaseOptionProcessors, errorPurchaseOptionProcessor, purchaseOptionOneofResolver);
    }

    @Configuration
    static class PurchaseOptionProcessorContext {
        @Bean
        ErrorPurchaseOptionProcessor errorPurchaseOptionProcessor() {
            return new ErrorPurchaseOptionProcessor();
        }

        @Bean
        PurchaseOptionProcessor<FullBookMetadataPurchaseOption> fullMetadataPurchaseOptionProcessor(
                final FindBookPort findBookPort,
                final UpdateBookPort updateBookPort
        ) {
            return new FullMetadataPurchaseOptionProcessor(findBookPort, updateBookPort);
        }
    }

    @Configuration
    static class PurchaseOptionOneofResolverContext {
        @Bean
        @Primary
        OneofResolver<PurchaseBookRequest, PurchaseOption<?>> purchaseOptionOneofResolver(
                final List<AbstractOneofResolver<PurchaseBookRequest, PurchaseOption<?>>> purchaseOptionsOneofResolvers
        ) {
            return AbstractOneofResolver.createChain(purchaseOptionsOneofResolvers);
        }

        @Bean
        AbstractOneofResolver<PurchaseBookRequest, PurchaseOption<?>> fullMetadataPurchaseOptionOneofResolver() {
            return new FullMetadataPurchaseOptionOneofResolver();
        }
    }
}
