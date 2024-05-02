package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.supply;

import com.sokima.reactive.grpc.bookstore.domain.Book;
import com.sokima.reactive.grpc.bookstore.domain.BookIdentity;
import com.sokima.reactive.grpc.bookstore.domain.Isbn;
import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.port.CreateBookPort;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.test.workflow.SupplyBookWorkflowTestContext;
import com.sokima.reactive.grpc.bookstore.proto.SupplyBookRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SupplyBookWorkflowTestContext.class)
class SupplyBookWorkflowIT {

    @Autowired
    SupplyBookWorkflow supplyBookWorkflow;

    @MockBean
    FindBookPort findBookPortMock;

    @MockBean
    CreateBookPort createBookPortMock;

    @Mock
    BookIdentity bookIdentityMock;

    @Mock
    Book bookMock;

    @Mock
    Isbn isbnMock;

    @Test
    void testAutowiring() {
        Assertions.assertNotNull(supplyBookWorkflow);
    }

    @Test
    void doWorkflow_supply3Books_SupplyFlowResult() {
        final var title = "title-test";
        final var author = "author-test";
        final var edition = "edition-test";
        final var checksum = ChecksumGenerator.generateBookChecksum(title, author, edition);
        final var supplyBookRequest = SupplyBookRequest.newBuilder()
                .setTitle(title)
                .setAuthor(author)
                .setEdition(edition)
                .setSupplyNumber(3)
                .build();

        Mockito.when(findBookPortMock.findBookByChecksum(checksum))
                .thenReturn(Mono.just(bookIdentityMock));

        Mockito.when(bookIdentityMock.author()).thenReturn(author);
        Mockito.when(bookIdentityMock.title()).thenReturn(title);
        Mockito.when(bookIdentityMock.edition()).thenReturn(edition);

        Mockito.when(createBookPortMock.createBookN(bookIdentityMock, 3))
                .thenReturn(Flux.just(bookMock, bookMock, bookMock));
        Mockito.when(bookMock.isbn()).thenReturn(isbnMock);
        Mockito.when(isbnMock.isbn()).thenReturn("1234567");
        Mockito.when(bookMock.bookIdentity()).thenReturn(bookIdentityMock);

        supplyBookWorkflow.doWorkflow(supplyBookRequest)
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualSupplyBookResponse -> {
                    Assertions.assertNotNull(actualSupplyBookResponse);

                    Assertions.assertEquals(checksum, actualSupplyBookResponse.getChecksum().getValue());
                    Assertions.assertEquals(3, actualSupplyBookResponse.getQuantity());

                    final var isbnList = actualSupplyBookResponse.getIsbnList();
                    Assertions.assertFalse(isbnList.isEmpty());
                    Assertions.assertEquals(3, isbnList.size());
                })
                .verifyComplete();
    }
}
