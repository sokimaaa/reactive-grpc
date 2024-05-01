package com.sokima.reactive.grpc.bookstore.infrastructure.context.workflow;

import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.search.SearchOptionOneof;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.search.partial.PartialMetadataOneof;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.proto.GetBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.PartialBookMetadata;
import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.get.GetBookFlow;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.*;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.get.processor.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class GetBookWorkflowContext {

    @Bean
    Flow<SearchOption<?>, GetBookFlowResult> getBookFlow(
            List<BookOptionProcessor<SearchOption<?>>> processors,
            ErrorBookOptionProcessor<SearchOption<?>> fallbackProcessor
    ) {
        return new GetBookFlow(processors, fallbackProcessor);
    }

    @Bean
    @Primary
    SearchOptionOneof<?> searchOptionOneof(final List<AbstractOneofResolver<GetBookRequest, SearchOption<?>>> searchOneofResolvers) {
        return (SearchOptionOneof) AbstractOneofResolver.createChain(searchOneofResolvers);
    }

    @Bean
    @Primary
    PartialMetadataOneof partialMetadataOneof(final List<AbstractOneofResolver<PartialBookMetadata, SearchOption<String>>> partialSearchOneofResolvers) {
        return (PartialMetadataOneof) AbstractOneofResolver.createChain(partialSearchOneofResolvers);
    }

    @Bean
    ErrorBookOptionProcessor<SearchOption<?>> errorBookOptionProcessor() {
        return new ErrorBookOptionProcessor<>();
    }

    @Bean
    BookOptionProcessor<AuthorSearchOption> authorBookOptionBookOptionProcessor(
            final FindBookPort findBookPort
    ) {
        return new AuthorBookOptionProcessor(findBookPort);
    }

    @Bean
    BookOptionProcessor<ChecksumSearchOption> checksumBookOptionBookOptionProcessor(
            final FindBookPort findBookPort
    ) {
        return new ChecksumBookOptionProcessor(findBookPort);
    }

    @Bean
    BookOptionProcessor<FullMetadataSearchOption> fullMetadataBookOptionBookOptionProcessor(
            final BookOptionProcessor<ChecksumSearchOption> checksumBookOptionBookOptionProcessor
    ) {
        return new FullMetadataBookOptionProcessor(checksumBookOptionBookOptionProcessor);
    }

    @Bean
    BookOptionProcessor<TitleSearchOption> titleBookOptionProcessor(
            final FindBookPort findBookPort
    ) {
        return new TitleBookOptionProcessor(findBookPort);
    }
}
