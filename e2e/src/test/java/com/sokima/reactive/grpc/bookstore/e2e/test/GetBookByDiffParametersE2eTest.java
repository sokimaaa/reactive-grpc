package com.sokima.reactive.grpc.bookstore.e2e.test;

import com.sokima.reactive.grpc.bookstore.e2e.E2eContainer;
import com.sokima.reactive.grpc.bookstore.e2e.client.GetBookClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
class GetBookByDiffParametersE2eTest extends E2eContainer {

    @Autowired
    GetBookClient getBookClient;

    @Test
    void getBookUseCase_titlePartialSearch_foundBooks() {
        getBookClient.setTitlePartialMetadataRequest("1984")
                .invokeGetBookStub()
                .log()
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void getBookUseCase_checksumSearch_foundBooks() {
        getBookClient.setChecksumRequest("d3b9af9e8a72cc6d0725924a4bd1cc5f")
                .invokeGetBookStub()
                .log()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getBookUseCase_authorPartialSearch_foundBooks() {
        getBookClient.setAuthorPartialMetadataRequest("George Orwell")
                .invokeGetBookStub()
                .log()
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void getBookUseCase_fullMetadataSearch_foundBook() {
        getBookClient.setFullMetadataRequest(
                        "Pride and Prejudice",
                        "Jane Austen",
                        "3rd Edition"
                )
                .invokeGetBookStub()
                .log()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }
}
