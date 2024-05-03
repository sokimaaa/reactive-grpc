package com.sokima.reactive.grpc.bookstore.usecase.get;

import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.get.processor.BookOptionProcessor;
import com.sokima.reactive.grpc.bookstore.usecase.get.processor.ErrorBookOptionProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class GetBookFlowTest {

    @Mock
    BookOptionProcessor<SearchOption<?>> firstProcessorMock;

    @Mock
    BookOptionProcessor<SearchOption<?>> secondProcessorMock;

    @Mock
    ErrorBookOptionProcessor<SearchOption<?>> fallbackProcessorMock;

    @Mock
    GetBookFlowResult getBookFlowResultMock;

    @Mock
    SearchOption<?> searchOptionMock;

    GetBookFlow getBookFlow;

    @BeforeEach
    void setUp() {
        getBookFlow = new GetBookFlow(List.of(firstProcessorMock, secondProcessorMock), fallbackProcessorMock);
    }

    @Test
    void doFlow_processorSupport_processor() {
        Mockito.when(firstProcessorMock.support(anyString())).thenReturn(Boolean.FALSE);
        Mockito.when(secondProcessorMock.support(anyString())).thenReturn(Boolean.TRUE);

        Mockito.when(secondProcessorMock.safeCastAndProcess(any())).thenReturn(Flux.just(getBookFlowResultMock, getBookFlowResultMock, getBookFlowResultMock));

        Mockito.when(searchOptionMock.type()).thenReturn("");

        getBookFlow.doFlow(searchOptionMock)
                .log()
                .as(StepVerifier::create)
                .expectNextCount(3)
                .verifyComplete();

        Mockito.verify(firstProcessorMock, Mockito.times(1)).support(anyString());
        Mockito.verify(secondProcessorMock, Mockito.times(1)).support(anyString());
        Mockito.verify(secondProcessorMock, Mockito.times(1)).safeCastAndProcess(any());
    }

    @Test
    void doFlow_anyProcessorSupport_fallbackProcessor() {
        Mockito.when(firstProcessorMock.support(anyString())).thenReturn(Boolean.FALSE);
        Mockito.when(secondProcessorMock.support(anyString())).thenReturn(Boolean.FALSE);

        Mockito.when(fallbackProcessorMock.safeCastAndProcess(any()))
                .thenReturn(Flux.error(new UnsupportedOperationException()));

        Mockito.when(searchOptionMock.type()).thenReturn("");

        getBookFlow.doFlow(searchOptionMock)
                .log()
                .as(StepVerifier::create)
                .verifyError();


        Mockito.verify(firstProcessorMock, Mockito.times(1)).support(anyString());
        Mockito.verify(secondProcessorMock, Mockito.times(1)).support(anyString());
        Mockito.verify(fallbackProcessorMock, Mockito.times(1)).safeCastAndProcess(any());
    }
}