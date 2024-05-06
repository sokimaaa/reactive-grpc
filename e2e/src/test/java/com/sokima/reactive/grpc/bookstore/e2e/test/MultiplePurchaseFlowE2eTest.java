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
    void purchaseBook_twoDiffBookSupplied_bookIsNotAvailableAfterPurchase() {
        registrationBookClient.setRegistrationBookRequest(
                        "The Adventures of Huckleberry Finn",
                        "Mark Twain",
                        "4th Edition"
                )
                .invokeRegistrationBookStub()
                .log()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        registrationBookClient.setRegistrationBookRequest(
                        "Anna Karenina",
                        "Leo Tolstoy",
                        "Classic Edition"
                )
                .invokeRegistrationBookStub()
                .log()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        supplyBookClient.setSupplyBookRequest(
                        "The Adventures of Huckleberry Finn",
                        "Mark Twain",
                        "4th Edition",
                        1
                )
                .setSupplyBookRequest(
                        "Anna Karenina",
                        "Leo Tolstoy",
                        "Classic Edition",
                        1
                )
                .invokeSupplyBookStub()
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(supplyBookResponse -> {
                    Assertions.assertEquals(1, supplyBookResponse.getQuantity());
                })
                .consumeNextWith(supplyBookResponse -> {
                    Assertions.assertEquals(1, supplyBookResponse.getQuantity());
                })
                .verifyComplete();

        purchaseBookClient.setFullMetadataRequest(
                        "Anna Karenina",
                        "Leo Tolstoy",
                        "Classic Edition"
                )
                .setFullMetadataRequest(
                        "The Adventures of Huckleberry Finn",
                        "Mark Twain",
                        "4th Edition"
                )
                .invokePurchaseBookStub()
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(purchaseBookResponse -> {
                    Assertions.assertEquals(2, purchaseBookResponse.getPurchasedBooksCount());
                })
                .verifyComplete();

        getBookClient.setFullMetadataRequest(
                        "The Adventures of Huckleberry Finn",
                        "Mark Twain",
                        "4th Edition"
                )
                .invokeGetBookStub()
                .log()
                .as(StepVerifier::create)
                .consumeNextWith(getBookResponse -> {
                    Assertions.assertFalse(getBookResponse.getIsAvailable());
                })
                .verifyComplete();

        getBookClient.setFullMetadataRequest(
                        "Anna Karenina",
                        "Leo Tolstoy",
                        "Classic Edition"
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
