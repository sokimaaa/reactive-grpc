package com.sokima.reactive.grpc.bookstore.usecase.registration.mapper;

import com.sokima.reactive.grpc.bookstore.usecase.registration.out.ImmutableRegistrationBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.registration.out.RegistrationBookFlowResult;

public class Checksum2RegistrationFlowResultMapper {
    public RegistrationBookFlowResult mapToRegistrationFlowResult(final String checksum) {
        return ImmutableRegistrationBookFlowResult.builder()
                .checksum(checksum)
                .build();
    }
}
