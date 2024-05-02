package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get;

import com.sokima.reactive.grpc.bookstore.domain.BookAggregation;
import com.sokima.reactive.grpc.bookstore.domain.BookIdentity;
import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.port.FindBookPort;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.test.workflow.GetBookWorkflowTestContext;
import com.sokima.reactive.grpc.bookstore.proto.Checksum;
import com.sokima.reactive.grpc.bookstore.proto.FullBookMetadata;
import com.sokima.reactive.grpc.bookstore.proto.GetBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.PartialBookMetadata;
import org.apache.logging.log4j.util.Strings;
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
@ContextConfiguration(classes = GetBookWorkflowTestContext.class)
class GetBookWorkflowIT {

    @Autowired
    GetBookWorkflow getBookWorkflow;

    @MockBean
    FindBookPort findBookPortMock;

    @Mock
    BookIdentity bookIdentityMock;

    @Mock
    BookAggregation bookAggregationMock;

    @Test
    void testAutowiring() {
        Assertions.assertNotNull(getBookWorkflow);
    }

    @Test
    void doWorkflow_checksumSearchOption_getBookResponse() {
        final var title = "title-test";
        final var author = "author-test";
        final var edition = "edition-test";
        final var checksum = ChecksumGenerator.generateBookChecksum(title, author, edition);
        final GetBookRequest getBookRequest = GetBookRequest.newBuilder()
                .setBookChecksum(
                        Checksum.newBuilder().setValue(checksum).build()
                )
                .build();

        Mockito.when(findBookPortMock.findBookByChecksum(checksum))
                .thenReturn(Mono.just(bookIdentityMock));

        Mockito.when(bookIdentityMock.author()).thenReturn(author);
        Mockito.when(bookIdentityMock.edition()).thenReturn(edition);
        Mockito.when(bookIdentityMock.title()).thenReturn(title);
        Mockito.when(bookIdentityMock.description()).thenReturn(Strings.EMPTY);

        Mockito.when(findBookPortMock.findBookAggregationByChecksum(checksum))
                .thenReturn(Mono.just(bookAggregationMock));

        Mockito.when(bookAggregationMock.checksum()).thenReturn(checksum);
        Mockito.when(bookAggregationMock.quantity()).thenReturn(1L);


        getBookWorkflow.doWorkflow(getBookRequest)
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualResponse -> {
                    Assertions.assertNotNull(actualResponse);

                    Assertions.assertEquals(checksum, actualResponse.getChecksum().getValue());
                    Assertions.assertEquals(author, actualResponse.getAuthor());
                    Assertions.assertEquals(title, actualResponse.getTitle());
                    Assertions.assertEquals(edition, actualResponse.getEdition());
                    Assertions.assertTrue(actualResponse.getIsAvailable());
                })
                .verifyComplete();
    }

    @Test
    void doWorkflow_fullMetadataSearchOption_getBookResponse() {
        final var title = "title-test";
        final var author = "author-test";
        final var edition = "edition-test";
        final var checksum = ChecksumGenerator.generateBookChecksum(title, author, edition);
        final GetBookRequest getBookRequest = GetBookRequest.newBuilder()
                .setFullBookMetadata(
                        FullBookMetadata.newBuilder()
                                .setAuthor(author)
                                .setTitle(title)
                                .setEdition(edition)
                                .build()
                )
                .build();

        Mockito.when(findBookPortMock.findBookByChecksum(checksum))
                .thenReturn(Mono.just(bookIdentityMock));

        Mockito.when(bookIdentityMock.author()).thenReturn(author);
        Mockito.when(bookIdentityMock.edition()).thenReturn(edition);
        Mockito.when(bookIdentityMock.title()).thenReturn(title);
        Mockito.when(bookIdentityMock.description()).thenReturn(Strings.EMPTY);

        Mockito.when(findBookPortMock.findBookAggregationByChecksum(checksum))
                .thenReturn(Mono.just(bookAggregationMock));

        Mockito.when(bookAggregationMock.checksum()).thenReturn(checksum);
        Mockito.when(bookAggregationMock.quantity()).thenReturn(1L);


        getBookWorkflow.doWorkflow(getBookRequest)
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualResponse -> {
                    Assertions.assertNotNull(actualResponse);

                    Assertions.assertEquals(checksum, actualResponse.getChecksum().getValue());
                    Assertions.assertEquals(author, actualResponse.getAuthor());
                    Assertions.assertEquals(title, actualResponse.getTitle());
                    Assertions.assertEquals(edition, actualResponse.getEdition());
                    Assertions.assertTrue(actualResponse.getIsAvailable());
                })
                .verifyComplete();
    }

    @Test
    void doWorkflow_titleSearchOption_getBookResponse() {
        final var title = "title-test";
        final var author = "author-test";
        final var edition = "edition-test";
        final var checksum = ChecksumGenerator.generateBookChecksum(title, author, edition);
        final GetBookRequest getBookRequest = GetBookRequest.newBuilder()
                .setPartialBookMetadata(
                        PartialBookMetadata.newBuilder()
                                .setTitle(title)
                                .build()
                )
                .build();

        Mockito.when(findBookPortMock.findBooksByTitle(title))
                .thenReturn(Flux.just(bookIdentityMock, bookIdentityMock));

        Mockito.when(bookIdentityMock.author()).thenReturn(author);
        Mockito.when(bookIdentityMock.edition()).thenReturn(edition);
        Mockito.when(bookIdentityMock.title()).thenReturn(title);
        Mockito.when(bookIdentityMock.description()).thenReturn(Strings.EMPTY);

        Mockito.when(findBookPortMock.findBookAggregationByChecksum(checksum))
                .thenReturn(Mono.just(bookAggregationMock));

        Mockito.when(bookAggregationMock.checksum()).thenReturn(checksum);
        Mockito.when(bookAggregationMock.quantity()).thenReturn(1L);


        getBookWorkflow.doWorkflow(getBookRequest)
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualResponse -> {
                    Assertions.assertNotNull(actualResponse);

                    Assertions.assertEquals(checksum, actualResponse.getChecksum().getValue());
                    Assertions.assertEquals(author, actualResponse.getAuthor());
                    Assertions.assertEquals(title, actualResponse.getTitle());
                    Assertions.assertEquals(edition, actualResponse.getEdition());
                    Assertions.assertTrue(actualResponse.getIsAvailable());
                })
                .expectNextCount(1L)
                .verifyComplete();
    }

    @Test
    void doWorkflow_authorSearchOption_getBookResponse() {
        final var title = "title-test";
        final var author = "author-test";
        final var edition = "edition-test";
        final var checksum = ChecksumGenerator.generateBookChecksum(title, author, edition);
        final GetBookRequest getBookRequest = GetBookRequest.newBuilder()
                .setPartialBookMetadata(
                        PartialBookMetadata.newBuilder()
                                .setAuthor(author)
                                .build()
                )
                .build();

        Mockito.when(findBookPortMock.findBooksByAuthor(author))
                .thenReturn(Flux.just(bookIdentityMock, bookIdentityMock));

        Mockito.when(bookIdentityMock.author()).thenReturn(author);
        Mockito.when(bookIdentityMock.edition()).thenReturn(edition);
        Mockito.when(bookIdentityMock.title()).thenReturn(title);
        Mockito.when(bookIdentityMock.description()).thenReturn(Strings.EMPTY);

        Mockito.when(findBookPortMock.findBookAggregationByChecksum(checksum))
                .thenReturn(Mono.just(bookAggregationMock));

        Mockito.when(bookAggregationMock.checksum()).thenReturn(checksum);
        Mockito.when(bookAggregationMock.quantity()).thenReturn(1L);


        getBookWorkflow.doWorkflow(getBookRequest)
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualResponse -> {
                    Assertions.assertNotNull(actualResponse);

                    Assertions.assertEquals(checksum, actualResponse.getChecksum().getValue());
                    Assertions.assertEquals(author, actualResponse.getAuthor());
                    Assertions.assertEquals(title, actualResponse.getTitle());
                    Assertions.assertEquals(edition, actualResponse.getEdition());
                    Assertions.assertTrue(actualResponse.getIsAvailable());
                })
                .expectNextCount(1L)
                .verifyComplete();
    }

    @Test
    void doWorkflow_unknownSearchType_Exception() {
        final GetBookRequest getBookRequest = GetBookRequest.newBuilder().build();

        getBookWorkflow.doWorkflow(getBookRequest)
                .log()
                .as(StepVerifier::create)
                .expectErrorMatches(actualThrowable -> actualThrowable instanceof UnsupportedOperationException)
                .verify();
    }
}
