package com.sokima.reactive.grpc.bookstore.infrastructure.context.workflow;

import com.sokima.reactive.grpc.bookstore.domain.port.UpdateBookPort;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update.option.UpdateOptionOneof;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.update.UpdateBookFlow;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.DescriptionUpdateOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.UpdateOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.out.UpdateBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.update.processor.DescriptionUpdateOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.update.processor.ErrorUpdateOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.update.processor.UpdateOptionProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class UpdateBookWorkflowContext {

    @Bean
    Flow<UpdateOption, UpdateBookFlowResult> updateBookFlow(
            List<UpdateOptionProcessor<UpdateOption>> processors,
            ErrorUpdateOptionProcessor fallbackProcessor
    ) {
        return new UpdateBookFlow(processors, fallbackProcessor);
    }

    @Bean
    @Primary
    UpdateOptionOneof updateOptionOneof(
            final List<AbstractOneofResolver<UpdateBookRequest, UpdateOption>> updateOptionBookResolvers
    ) {
        return (UpdateOptionOneof) AbstractOneofResolver.createChain(updateOptionBookResolvers);
    }

    @Bean
    ErrorUpdateOptionProcessor errorUpdateOptionProcessor() {
        return new ErrorUpdateOptionProcessor();
    }

    @Bean
    UpdateOptionProcessor<DescriptionUpdateOption> descriptionUpdateOptionProcessor(
            final UpdateBookPort updateBookPort
    ) {
        return new DescriptionUpdateOptionProcessor(updateBookPort);
    }
}
