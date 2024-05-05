package com.sokima.reactive.grpc.bookstore.e2e.test;

import com.sokima.reactive.grpc.bookstore.e2e.E2eContainer;
import com.sokima.reactive.grpc.bookstore.e2e.client.GetBookClient;
import com.sokima.reactive.grpc.bookstore.e2e.client.PurchaseBookClient;
import com.sokima.reactive.grpc.bookstore.e2e.client.RegistrationBookClient;
import com.sokima.reactive.grpc.bookstore.e2e.client.SupplyBookClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
class MultiplePurchaseFlowE2eTest extends E2eContainer {

    @Autowired
    RegistrationBookClient registrationBookClient;

    @Autowired
    SupplyBookClient supplyBookClient;

    @Autowired
    PurchaseBookClient purchaseBookClient;

    @Autowired
    GetBookClient getBookClient;

    @Test
    void purchaseBook_twoBookSupplied_bookIsNotAvailableAfterPurchase() {
        registrationBookClient.setRegistrationBookRequest(
                        "Great Expectations",
                        "Charles Dickens",
                        "Illustrated Edition"
                )
                .invokeRegistrationBookStub()
                .log()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        supplyBookClient.setSupplyBookRequest(
                        "Great Expectations",
                        "Charles Dickens",
                        "Illustrated Edition",
                        2
                )
                .invokeSupplyBookStub()
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(supplyBookResponse -> {
                    Assertions.assertEquals(2, supplyBookResponse.getQuantity());
                })
                .verifyComplete();

        purchaseBookClient.setFullMetadataRequest(
                        "Great Expectations",
                        "Charles Dickens",
                        "Illustrated Edition"
                )
                .setFullMetadataRequest(
                        "Great Expectations",
                        "Charles Dickens",
                        "Illustrated Edition"
                )
                .invokePurchaseBookStub()
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(purchaseBookResponse -> {
                    Assertions.assertEquals(2, purchaseBookResponse.getPurchasedBooksCount());
                })
                .verifyComplete();

        getBookClient.setFullMetadataRequest(
                        "Crime and Punishment",
                        "Fyodor Dostoevsky",
                        "Vintage Edition"
                )
                .invokeGetBookStub()
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(getBookResponse -> {
                    Assertions.assertFalse(getBookResponse.getIsAvailable());
                })
                .verifyComplete();
    }
}
