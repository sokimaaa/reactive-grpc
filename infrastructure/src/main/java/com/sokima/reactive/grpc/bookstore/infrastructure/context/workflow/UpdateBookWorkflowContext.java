package com.sokima.reactive.grpc.bookstore.infrastructure.context.workflow;

import com.sokima.reactive.grpc.bookstore.domain.port.UpdateBookPort;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update.option.UpdateOptionOneof;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update.transformer.field.FieldResponseOneof;
import com.sokima.reactive.grpc.bookstore.proto.BookField;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.update.UpdateBookFlow;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.DescriptionUpdateOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.UpdateOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.out.UpdateBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.update.processor.DescriptionUpdateOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.update.processor.ErrorUpdateOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.update.processor.UpdateOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.update.processor.mapper.Container2UpdateFlowResultMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class UpdateBookWorkflowContext {

    @Bean
    Flow<UpdateOption, UpdateBookFlowResult> updateBookFlow(
            List<UpdateOptionProcessor<? extends UpdateOption>> processors,
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
    @Primary
    FieldResponseOneof fieldResponseOneof(
            final List<AbstractOneofResolver<UpdateOption, BookField>> fieldResponseOneofResolvers
    ) {
        return (FieldResponseOneof) AbstractOneofResolver.createChain(fieldResponseOneofResolvers);
    }

    @Bean
    ErrorUpdateOptionProcessor errorUpdateOptionProcessor() {
        return new ErrorUpdateOptionProcessor();
    }

    @Bean
    UpdateOptionProcessor<DescriptionUpdateOption> descriptionUpdateOptionProcessor(
            final UpdateBookPort updateBookPort, final Container2UpdateFlowResultMapper containerMapper
    ) {
        return new DescriptionUpdateOptionProcessor(updateBookPort, containerMapper);
    }

    @Bean
    Container2UpdateFlowResultMapper container2UpdateFlowResultMapper() {
        return new Container2UpdateFlowResultMapper();
    }
}
