package com.sokima.reactive.grpc.bookstore.infrastructure.context.common;

import com.sokima.reactive.grpc.bookstore.domain.helper.CommonTransformer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransformerContext {

    @Bean
    CommonTransformer commonTransformer() {
        return new CommonTransformer();
    }
}
