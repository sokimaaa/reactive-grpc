package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.test;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@TestConfiguration
@ComponentScan("com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common")
@Import(InfrastructureCommonTestContext.class)
public class GrpcAdapterCommonTestContext {
}
