package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.registration;

import com.sokima.reactive.grpc.bookstore.domain.BookAggregation;
import com.sokima.reactive.grpc.bookstore.domain.BookIdentity;
import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.port.CreateBookPort;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.test.workflow.RegistrationBookWorkflowTestContext;
import com.sokima.reactive.grpc.bookstore.proto.RegistrationBookRequest;
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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RegistrationBookWorkflowTestContext.class)
class RegistrationBookWorkflowIT {

    @Autowired
    RegistrationBookWorkflow registrationBookWorkflow;

    @MockBean
    CreateBookPort createBookPortMock;

    @Mock
    BookIdentity bookIdentityMock;

    @Mock
    BookAggregation bookAggregationMock;

    @Test
    void testAutowiring() {
        Assertions.assertNotNull(registrationBookWorkflow);
    }

    @Test
    void doWorkflow_registerNewBook_RegistrationFlowResult() {
        final var title = "title-test";
        final var author = "author-test";
        final var edition = "edition-test";
        final var checksum = ChecksumGenerator.generateBookChecksum(title, author, edition);
        final var registrationBookRequest = RegistrationBookRequest.newBuilder()
                .setTitle(title)
                .setAuthor(author)
                .setEdition(edition)
                .build();

        Mockito.when(createBookPortMock.createBookIdentity(any(BookIdentity.class)))
                .thenReturn(Mono.just(bookIdentityMock));

        Mockito.when(bookIdentityMock.author()).thenReturn(author);
        Mockito.when(bookIdentityMock.title()).thenReturn(title);
        Mockito.when(bookIdentityMock.edition()).thenReturn(edition);

        Mockito.when(createBookPortMock.createEmptyBookAggregation(checksum))
                .thenReturn(Mono.just(bookAggregationMock));

        Mockito.when(bookAggregationMock.checksum()).thenReturn(checksum);

        registrationBookWorkflow.doWorkflow(registrationBookRequest)
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(actualRegistrationBookResponse -> {
                    Assertions.assertNotNull(actualRegistrationBookResponse);
                    Assertions.assertEquals(checksum, actualRegistrationBookResponse.getChecksum().getValue());
                })
                .verifyComplete();
    }
}
