package com.sokima.reactive.grpc.bookstore.infrastructure.adapter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
class ContextLoadTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void loadContext() {
        Assertions.assertNotNull(applicationContext);
    }
}
