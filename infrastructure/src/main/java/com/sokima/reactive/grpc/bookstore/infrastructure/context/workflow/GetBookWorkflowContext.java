package com.sokima.reactive.grpc.bookstore.infrastructure.context.workflow;

import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof.AbstractOneofResolver;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.search.SearchOptionOneof;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.search.partial.PartialMetadataOneof;
import com.sokima.reactive.grpc.bookstore.proto.GetBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.PartialBookMetadata;
import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.get.GetBookFlow;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.*;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.get.processor.*;
import com.sokima.reactive.grpc.bookstore.usecase.get.processor.mapper.Baggage2GetFlowResultMapper;
import com.sokima.reactive.grpc.bookstore.usecase.get.processor.mapper.FullMetadata2ChecksumOptionMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class GetBookWorkflowContext {

    @Bean
    Flow<SearchOption<?>, GetBookFlowResult> getBookFlow(
            final List<BookOptionProcessor<? extends SearchOption<?>>> processors,
            final ErrorBookOptionProcessor<SearchOption<?>> fallbackProcessor
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
            final FindBookPort findBookPort, final Baggage2GetFlowResultMapper baggageMapper
    ) {
        return new AuthorBookOptionProcessor(findBookPort, baggageMapper);
    }

    @Bean
    BookOptionProcessor<ChecksumSearchOption> checksumBookOptionBookOptionProcessor(
            final FindBookPort findBookPort, final Baggage2GetFlowResultMapper baggageMapper
    ) {
        return new ChecksumBookOptionProcessor(findBookPort, baggageMapper);
    }

    @Bean
    BookOptionProcessor<FullMetadataSearchOption> fullMetadataBookOptionBookOptionProcessor(
            final BookOptionProcessor<ChecksumSearchOption> checksumBookOptionBookOptionProcessor,
            final FullMetadata2ChecksumOptionMapper fullMetadata2ChecksumOptionMapper
    ) {
        return new FullMetadataBookOptionProcessor(checksumBookOptionBookOptionProcessor, fullMetadata2ChecksumOptionMapper);
    }

    @Bean
    BookOptionProcessor<TitleSearchOption> titleBookOptionProcessor(
            final FindBookPort findBookPort, final Baggage2GetFlowResultMapper baggageMapper
    ) {
        return new TitleSearchOptionProcessor(findBookPort, baggageMapper);
    }

    @Bean
    Baggage2GetFlowResultMapper baggage2GetFlowResultMapper() {
        return new Baggage2GetFlowResultMapper();
    }

    @Bean
    FullMetadata2ChecksumOptionMapper fullMetadata2ChecksumOptionMapper() {
        return new FullMetadata2ChecksumOptionMapper();
    }
}
