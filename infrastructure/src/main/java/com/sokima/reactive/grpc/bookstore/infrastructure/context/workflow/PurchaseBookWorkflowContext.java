package com.sokima.reactive.grpc.bookstore.infrastructure.context.workflow;

import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.domain.port.UpdateBookPort;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.purchase.option.PurchaseOptionOneof;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.PurchaseBookFlow;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.FullBookMetadataPurchaseOption;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.PurchaseOption;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.out.PurchaseBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.processor.ErrorPurchaseOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.processor.FullMetadataPurchaseOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.processor.PurchaseOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.processor.mapper.Container2PurchaseFlowResultMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class PurchaseBookWorkflowContext {

    @Bean
    Flow<PurchaseOption<?>, List<PurchaseBookFlowResult>> purchaseBookFlow(
            final List<PurchaseOptionProcessor<? extends PurchaseOption<?>>> processors,
            final ErrorPurchaseOptionProcessor<PurchaseOption<?>> fallbackProcessor
    ) {
        return new PurchaseBookFlow(processors, fallbackProcessor);
    }

    @Bean
    @Primary
    PurchaseOptionOneof<?> purchaseOptionOneof(
            final List<AbstractOneofResolver<PurchaseBookRequest, PurchaseOption<?>>> purchaseOneofResolvers
    ) {
        return (PurchaseOptionOneof) AbstractOneofResolver.createChain(purchaseOneofResolvers);
    }

    @Bean
    ErrorPurchaseOptionProcessor<PurchaseOption<?>> errorPurchaseOptionProcessor() {
        return new ErrorPurchaseOptionProcessor<>();
    }

    @Bean
    PurchaseOptionProcessor<FullBookMetadataPurchaseOption> fullMetadataPurchaseOptionProcessor(
            final FindBookPort findBookPort,
            final UpdateBookPort updateBookPort,
            final Container2PurchaseFlowResultMapper containerMapper
    ) {
        return new FullMetadataPurchaseOptionProcessor(findBookPort, updateBookPort, containerMapper);
    }

    @Bean
    Container2PurchaseFlowResultMapper container2PurchaseFlowResultMapper() {
        return new Container2PurchaseFlowResultMapper();
    }
}
