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
class SinglePurchaseFlowE2eTest extends E2eContainer {

    @Autowired
    RegistrationBookClient registrationBookClient;

    @Autowired
    SupplyBookClient supplyBookClient;

    @Autowired
    PurchaseBookClient purchaseBookClient;

    @Autowired
    GetBookClient getBookClient;

    @Test
    void purchaseBook_singleBookSupplied_bookIsNotAvailableAfterPurchase() {
        registrationBookClient.setRegistrationBookRequest(
                        "Crime and Punishment",
                        "Fyodor Dostoevsky",
                        "Vintage Edition"
                )
                .invokeRegistrationBookStub()
                .log()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        supplyBookClient.setSupplyBookRequest(
                        "Crime and Punishment",
                        "Fyodor Dostoevsky",
                        "Vintage Edition",
                        1
                )
                .invokeSupplyBookStub()
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(supplyBookResponse -> {
                    Assertions.assertEquals(1, supplyBookResponse.getQuantity());
                })
                .verifyComplete();

        purchaseBookClient.setFullMetadataRequest(
                        "Crime and Punishment",
                        "Fyodor Dostoevsky",
                        "Vintage Edition"
                )
                .invokePurchaseBookStub()
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(purchaseBookResponse -> {
                    Assertions.assertEquals(1, purchaseBookResponse.getPurchasedBooksCount());
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
