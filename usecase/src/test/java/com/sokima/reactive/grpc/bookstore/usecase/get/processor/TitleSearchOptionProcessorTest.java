package com.sokima.reactive.grpc.bookstore.usecase.get.processor;

import com.sokima.reactive.grpc.bookstore.domain.BookAggregation;
import com.sokima.reactive.grpc.bookstore.domain.BookIdentity;
import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.helper.Baggage;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.ImmutableTitleSearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.get.processor.mapper.Baggage2GetFlowResultMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class TitleSearchOptionProcessorTest {

    @InjectMocks TitleSearchOptionProcessor processor;

    @Mock
    FindBookPort findBookPortMock;

    @Mock
    Baggage2GetFlowResultMapper mapperMock;

    @Mock
    BookIdentity bookIdentityMock;

    @Mock
    BookAggregation bookAggregationMock;

    @Mock
    GetBookFlowResult getBookFlowResultMock;

    @Test
    void process_existingTitle_SingleFluxResult() {
        final String title = "title";
        final String author = "author";
        final String edition = "edition";
        final String checksum = ChecksumGenerator.generateBookChecksum(title, author, edition);

        Mockito.when(findBookPortMock.findBooksByTitle(title))
                .thenReturn(Flux.just(bookIdentityMock));

        Mockito.when(bookIdentityMock.title()).thenReturn(title);
        Mockito.when(bookIdentityMock.author()).thenReturn(author);
        Mockito.when(bookIdentityMock.edition()).thenReturn(edition);

        Mockito.when(findBookPortMock.findBookAggregationByChecksum(checksum))
                .thenReturn(Mono.just(bookAggregationMock));

        Mockito.when(mapperMock.mapToGetBookFlowResult(Baggage.of(bookIdentityMock, bookAggregationMock)))
                .thenReturn(getBookFlowResultMock);

        final var titleSearchOption = ImmutableTitleSearchOption.builder()
                .option(title)
                .type(title)
                .build();

        processor.process(titleSearchOption)
                .log()
                .as(StepVerifier::create)
                .expectNextCount(1L)
                .verifyComplete();

        Mockito.verify(findBookPortMock, Mockito.times(1)).findBooksByTitle(anyString());
        Mockito.verify(findBookPortMock, Mockito.times(1)).findBookAggregationByChecksum(anyString());
        Mockito.verify(mapperMock, Mockito.times(1)).mapToGetBookFlowResult(any());
    }

    @Test
    void process_notExistingTitle_EmptyFlux() {
        final String title = "title";
        Mockito.when(findBookPortMock.findBooksByTitle(title))
                .thenReturn(Flux.empty());

        final var titleSearchOption = ImmutableTitleSearchOption.builder()
                .option(title)
                .type(title)
                .build();

        processor.process(titleSearchOption)
                .log()
                .as(StepVerifier::create)
                .verifyComplete();

        Mockito.verify(findBookPortMock, Mockito.times(1)).findBooksByTitle(anyString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"TITLE", "title", "tiTle"})
    void support_titleSearchOption_supported(final String type) {
        final boolean isSupport = processor.support(type);

        Assertions.assertTrue(isSupport);
    }

    @ParameterizedTest
    @ValueSource(strings = {"checksum", "unknown", ""})
    void support_unknownSearchOption_notSupported(final String type) {
        final boolean isSupport = processor.support(type);

        Assertions.assertFalse(isSupport);
    }
}
