package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.purchase;

import com.sokima.reactive.grpc.bookstore.domain.Book;
import com.sokima.reactive.grpc.bookstore.domain.BookAggregation;
import com.sokima.reactive.grpc.bookstore.domain.Isbn;
import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.domain.port.UpdateBookPort;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.test.workflow.PurchaseBookWorkflowTestContext;
import com.sokima.reactive.grpc.bookstore.proto.FullBookMetadata;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PurchaseBookWorkflowTestContext.class)
class PurchaseBookWorkflowIT {

    @Autowired
    PurchaseBookWorkflow purchaseBookWorkflow;

    @MockBean
    FindBookPort findBookPortMock;

    @MockBean
    UpdateBookPort updateBookPortMock;

    @Mock
    Book bookMock;

    @Mock
    BookAggregation bookAggregationMock;

    @Mock
    Isbn isbnMock;

    @Test
    void testAutowiring() {
        Assertions.assertNotNull(purchaseBookWorkflow);
    }

    @Test
    void doWorkflow_fullMetadataPurchaseOption_PurchaseBookFlowResult() {
        final var title = "title-test";
        final var author = "author-test";
        final var edition = "edition-test";
        final var isbnValue = "123456";
        final var checksum = ChecksumGenerator.generateBookChecksum(title, author, edition);
        final var purchaseBookRequest = PurchaseBookRequest.newBuilder()
                .setFullBookMetadata(
                        FullBookMetadata.newBuilder()
                                .setTitle(title)
                                .setAuthor(author)
                                .setEdition(edition)
                                .build()
                )
                .build();

        Mockito.when(findBookPortMock.findBookAggregationByChecksum(checksum))
                        .thenReturn(Mono.just(bookAggregationMock));

        Mockito.when(updateBookPortMock.updateBookAggregationQuantity(any(), any()))
                        .thenReturn(Mono.just(new UpdateBookPort.Container<>(bookAggregationMock, bookAggregationMock, Boolean.TRUE)));

        Mockito.when(findBookPortMock.nextBookByChecksum(checksum))
                .thenReturn(Mono.just(bookMock));

        Mockito.when(updateBookPortMock.updateBookIsPurchasedField(any(Isbn.class), anyBoolean()))
                .thenReturn(Mono.just(new UpdateBookPort.Container<>(bookMock, bookMock, Boolean.TRUE)));

        Mockito.when(bookMock.isbn())
                .thenReturn(isbnMock);

        Mockito.when(isbnMock.isbn())
                .thenReturn(isbnValue);

        purchaseBookWorkflow.doWorkflow(purchaseBookRequest)
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualPurchaseBookResponse -> {
                    Assertions.assertNotNull(actualPurchaseBookResponse);

                    final var purchasedBooksList = actualPurchaseBookResponse.getPurchasedBooksList();
                    Assertions.assertFalse(purchasedBooksList.isEmpty());
                    Assertions.assertEquals(isbnValue, purchasedBooksList.get(0).getValue());
                })
                .verifyComplete();
    }

    @Test
    void doWorkflow_unknownPurchaseOption_Exception() {
        final var purchaseBookRequest = PurchaseBookRequest.newBuilder().build();

        purchaseBookWorkflow.doWorkflow(purchaseBookRequest)
                .log()
                .as(StepVerifier::create)
                .expectErrorMatches(actualThrowable -> actualThrowable instanceof UnsupportedOperationException)
                .verify();
    }
}
