package com.sokima.reactive.grpc.bookstore.infrastructure.context.usecase;

import com.sokima.reactive.grpc.bookstore.domain.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.domain.oneof.OneofResolver;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.proto.GetBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.PartialBookMetadata;
import com.sokima.reactive.grpc.bookstore.usecase.get.GetBookFacade;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.*;
import com.sokima.reactive.grpc.bookstore.usecase.get.processor.*;
import com.sokima.reactive.grpc.bookstore.usecase.get.resolver.option.ChecksumSearchOptionOneofResolver;
import com.sokima.reactive.grpc.bookstore.usecase.get.resolver.option.FullMetadataSearchOptionOneofResolver;
import com.sokima.reactive.grpc.bookstore.usecase.get.resolver.option.PartialSearchOptionOneofResolver;
import com.sokima.reactive.grpc.bookstore.usecase.get.resolver.option.partial.AuthorMetadataOneofResolver;
import com.sokima.reactive.grpc.bookstore.usecase.get.resolver.option.partial.TitleMetadataOneofResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class GetBookUseCaseContext {
    @Bean
    GetBookFacade getBookFacade(
            final List<BookOptionProcessor<?>> bookOptionProcessors,
            final ErrorBookOptionProcessor errorBookOptionProcessor,
            final OneofResolver<GetBookRequest, SearchBookOption<?>> searchOptionOneofResolver
    ) {
        return new GetBookFacade(bookOptionProcessors, errorBookOptionProcessor, searchOptionOneofResolver);
    }

    @Configuration
    static class BookOptionProcessorContext {
        @Bean
        ErrorBookOptionProcessor errorBookOptionProcessor() {
            return new ErrorBookOptionProcessor();
        }

        @Bean
        BookOptionProcessor<AuthorBookOption> authorBookOptionBookOptionProcessor(
                final FindBookPort findBookPort
        ) {
            return new AuthorBookOptionProcessor(findBookPort);
        }

        @Bean
        BookOptionProcessor<ChecksumBookOption> checksumBookOptionBookOptionProcessor(
                final FindBookPort findBookPort
        ) {
            return new ChecksumBookOptionProcessor(findBookPort);
        }

        @Bean
        BookOptionProcessor<FullMetadataBookOption> fullMetadataBookOptionBookOptionProcessor(
                final BookOptionProcessor<ChecksumBookOption> checksumBookOptionBookOptionProcessor
        ) {
            return new FullMetadataBookOptionProcessor(checksumBookOptionBookOptionProcessor);
        }

        @Bean
        BookOptionProcessor<TitleBookOption> titleBookOptionBookOptionProcessor(
                final FindBookPort findBookPort
        ) {
            return new TitleBookOptionProcessor(findBookPort);
        }
    }

    @Configuration
    static class SearchOptionOneofResolverContext {
        @Bean
        @Primary
        OneofResolver<GetBookRequest, SearchBookOption<?>> searchOptionOneofResolver(
                final List<AbstractOneofResolver<GetBookRequest, SearchBookOption<?>>> searchOptionOneofResolvers) {
            return AbstractOneofResolver.createChain(searchOptionOneofResolvers);
        }

        @Bean
        AbstractOneofResolver<GetBookRequest, SearchBookOption<?>> checksumSearchOptionOneofResolver() {
            return new ChecksumSearchOptionOneofResolver();
        }

        @Bean
        AbstractOneofResolver<GetBookRequest, SearchBookOption<?>> partialSearchOptionOneofResolver(final OneofResolver<PartialBookMetadata, SearchBookOption<String>> partialMetadataOneofResolver) {
            return new PartialSearchOptionOneofResolver(partialMetadataOneofResolver);
        }

        @Bean
        AbstractOneofResolver<GetBookRequest, SearchBookOption<?>> fullMetadataSearchOptionOneofResolver() {
            return new FullMetadataSearchOptionOneofResolver();
        }

        @Configuration
        static class PartialMetadataOneofResolverContext {
            @Bean
            @Primary
            OneofResolver<PartialBookMetadata, SearchBookOption<String>> partialMetadataOneofResolver(
                    final List<AbstractOneofResolver<PartialBookMetadata, SearchBookOption<String>>> partialMetadataOneofResolvers) {
                return AbstractOneofResolver.createChain(partialMetadataOneofResolvers);
            }

            @Bean
            AbstractOneofResolver<PartialBookMetadata, SearchBookOption<String>> authorMetadataOneofResolver() {
                return new AuthorMetadataOneofResolver();
            }

            @Bean
            AbstractOneofResolver<PartialBookMetadata, SearchBookOption<String>> titleMetadataOneofResolver() {
                return new TitleMetadataOneofResolver();
            }
        }
    }
}
