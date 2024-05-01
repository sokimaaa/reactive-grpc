package com.sokima.reactive.grpc.bookstore.usecase.update.processor.mapper;

import com.sokima.reactive.grpc.bookstore.domain.BookIdentity;
import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.port.UpdateBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.ImmutableDescriptionUpdateOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.UpdateOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.out.ImmutableUpdateBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.update.out.UpdateBookFlowResult;

public class Container2UpdateFlowResultMapper {
    public UpdateBookFlowResult mapToUpdateFlowResult(final UpdateBookPort.Container<BookIdentity> bookIdentityContainer) {
        final BookIdentity oldBookIdentity = bookIdentityContainer.oldDomainObject();
        final BookIdentity newBookIdentity = bookIdentityContainer.newDomainObject();
        return ImmutableUpdateBookFlowResult.builder()
                .checksum(ChecksumGenerator.generateBookChecksum(newBookIdentity))
                .oldField(buildBookFieldOption(oldBookIdentity))
                .newField(buildBookFieldOption(newBookIdentity))
                .build();
    }

    private UpdateOption buildBookFieldOption(final BookIdentity bookIdentity) {
        return ImmutableDescriptionUpdateOption.builder()
                .checksum(ChecksumGenerator.generateBookChecksum(bookIdentity))
                .value(bookIdentity.description())
                .build();
    }
}
