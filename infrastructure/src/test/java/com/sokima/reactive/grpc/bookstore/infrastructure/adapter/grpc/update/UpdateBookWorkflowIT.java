package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update;

import com.sokima.reactive.grpc.bookstore.domain.BookIdentity;
import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.port.UpdateBookPort;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.test.workflow.UpdateBookWorkflowTestContext;
import com.sokima.reactive.grpc.bookstore.proto.BookField;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookRequest;
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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UpdateBookWorkflowTestContext.class)
class UpdateBookWorkflowIT {

    @Autowired
    UpdateBookWorkflow updateBookWorkflow;

    @MockBean
    UpdateBookPort updateBookPortMock;

    @Mock
    BookIdentity oldBookIdentityMock;

    @Mock
    BookIdentity newBookIdentityMock;

    @Test
    void testAutowiring() {
        Assertions.assertNotNull(updateBookWorkflow);
    }

    @Test
    void doWorkflow_updateDescription_UpdateFlowResult() {
        final var title = "title-test";
        final var author = "author-test";
        final var edition = "edition-test";
        final var description = "description-test";
        final var checksum = ChecksumGenerator.generateBookChecksum(title, author, edition);
        final var updateBookRequest = UpdateBookRequest.newBuilder()
                .setAuthor(author)
                .setTitle(title)
                .setEdition(edition)
                .setUpdatedBookField(
                        BookField.newBuilder()
                                .setDescription(description)
                                .build()
                )
                .build();

        Mockito.when(updateBookPortMock.updateBookIdentityField(checksum, "DESCRIPTION", description))
                .thenReturn(Mono.just(new UpdateBookPort.Container<>(oldBookIdentityMock, newBookIdentityMock, Boolean.TRUE)));

        Mockito.when(oldBookIdentityMock.author()).thenReturn(author);
        Mockito.when(oldBookIdentityMock.title()).thenReturn(title);
        Mockito.when(oldBookIdentityMock.edition()).thenReturn(edition);
        Mockito.when(oldBookIdentityMock.description()).thenReturn(Strings.EMPTY);

        Mockito.when(newBookIdentityMock.author()).thenReturn(author);
        Mockito.when(newBookIdentityMock.title()).thenReturn(title);
        Mockito.when(newBookIdentityMock.edition()).thenReturn(edition);
        Mockito.when(newBookIdentityMock.description()).thenReturn(description);

        updateBookWorkflow.doWorkflow(updateBookRequest)
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualUpdateBookResponse -> {
                    Assertions.assertNotNull(actualUpdateBookResponse);

                    Assertions.assertEquals(checksum, actualUpdateBookResponse.getChecksum().getValue());

                    final var newBookField = actualUpdateBookResponse.getNewBookField();
                    final var oldBookField = actualUpdateBookResponse.getOldBookField();

                    Assertions.assertNotNull(newBookField);
                    Assertions.assertNotNull(oldBookField);

                    Assertions.assertNotEquals(oldBookField.getDescription(), newBookField.getDescription());
                    Assertions.assertEquals(description, newBookField.getDescription());
                })
                .verifyComplete();
    }

    @Test
    void doWorkflow_unknownUpdateOption_Exception() {
        final var updateBookRequest = UpdateBookRequest.newBuilder().build();

        updateBookWorkflow.doWorkflow(updateBookRequest)
                .log()
                .as(StepVerifier::create)
                .expectErrorMatches(actualThrowable -> actualThrowable instanceof UnsupportedOperationException)
                .verify();
    }
}
