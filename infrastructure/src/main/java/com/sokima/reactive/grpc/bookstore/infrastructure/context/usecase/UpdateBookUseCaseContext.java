package com.sokima.reactive.grpc.bookstore.infrastructure.context.usecase;

import com.sokima.reactive.grpc.bookstore.domain.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.domain.oneof.OneofResolver;
import com.sokima.reactive.grpc.bookstore.domain.port.UpdateBookPort;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.update.UpdateBookFacade;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.BookFieldOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.DescriptionBookFieldOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.processor.DescriptionUpdateOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.update.processor.ErrorUpdateOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.update.processor.UpdateOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.update.resolver.DescriptionUpdateOptionOneofResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class UpdateBookUseCaseContext {
    @Bean
    UpdateBookFacade updateBookFacade(
            final OneofResolver<UpdateBookRequest, BookFieldOption> updateOptionOneofResolver,
            final List<UpdateOptionProcessor<?>> updateOptionProcessors,
            final ErrorUpdateOptionProcessor errorUpdateOptionProcessor
    ) {
        return new UpdateBookFacade(updateOptionOneofResolver, updateOptionProcessors, errorUpdateOptionProcessor);
    }

    @Configuration
    static class UpdateOptionProcessorContext {
        @Bean
        ErrorUpdateOptionProcessor errorUpdateOptionProcessor() {
            return new ErrorUpdateOptionProcessor();
        }

        @Bean
        UpdateOptionProcessor<DescriptionBookFieldOption> descriptionUpdateOptionProcessor(
                final UpdateBookPort updateBookPort
        ) {
            return new DescriptionUpdateOptionProcessor(updateBookPort);
        }
    }

    @Configuration
    static class UpdateOptionOneofResolverContext {
        @Bean
        @Primary
        OneofResolver<UpdateBookRequest, BookFieldOption> updateOptionOneofResolver(
                final List<AbstractOneofResolver<UpdateBookRequest, BookFieldOption>> updateOptionOneofResolvers
        ) {
            return AbstractOneofResolver.createChain(updateOptionOneofResolvers);
        }

        @Bean
        AbstractOneofResolver<UpdateBookRequest, BookFieldOption> descriptionUpdateOptionOneofResolver() {
            return new DescriptionUpdateOptionOneofResolver();
        }
    }
}
