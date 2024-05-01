package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.registration.transformer;

import com.sokima.reactive.grpc.bookstore.domain.PartialBookIdentity;
import com.sokima.reactive.grpc.bookstore.domain.helper.BookIdentityMapper;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Proto2JavaTransformer;
import com.sokima.reactive.grpc.bookstore.proto.RegistrationBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.registration.in.ImmutableRegistrationBookInformation;
import com.sokima.reactive.grpc.bookstore.usecase.registration.in.RegistrationBookInformation;
import org.springframework.stereotype.Component;

@Component
public class Request2RegistrationBookInformationTransformer extends BookIdentityMapper
        implements Proto2JavaTransformer<RegistrationBookRequest, RegistrationBookInformation> {
    @Override
    public RegistrationBookInformation transform(final RegistrationBookRequest proto) {
        return ImmutableRegistrationBookInformation.builder()
                .partialBookIdentity((PartialBookIdentity) partialBookIdentity(
                        proto.getTitle(),
                        proto.getAuthor(),
                        proto.getEdition()
                ))
                .build();
    }
}
