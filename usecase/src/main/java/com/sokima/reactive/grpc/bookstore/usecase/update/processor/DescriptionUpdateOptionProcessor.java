package com.sokima.reactive.grpc.bookstore.usecase.update.processor;

import com.sokima.reactive.grpc.bookstore.domain.BookIdentity;
import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.helper.FieldOption;
import com.sokima.reactive.grpc.bookstore.domain.port.UpdateBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.BookFieldOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.DescriptionBookFieldOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.ImmutableDescriptionBookFieldOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.out.ImmutableUpdateBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.update.out.UpdateBookResponse;
import reactor.core.publisher.Flux;

public class DescriptionUpdateOptionProcessor implements UpdateOptionProcessor<DescriptionBookFieldOption> {
    private final UpdateBookPort updateBookPort;

    public DescriptionUpdateOptionProcessor(final UpdateBookPort updateBookPort) {
        this.updateBookPort = updateBookPort;
    }

    @Override
    public Flux<UpdateBookResponse> process(final DescriptionBookFieldOption bookFieldOption) {
        return updateBookPort.updateBookIdentityField(bookFieldOption.checksum(), bookFieldOption.field(), bookFieldOption.value())
                .<UpdateBookResponse>map(bookIdentityContainer -> {
                    final BookIdentity oldBookIdentity = bookIdentityContainer.oldDomainObject();
                    final BookIdentity newBookIdentity = bookIdentityContainer.newDomainObject();
                    return ImmutableUpdateBookResponse.builder()
                            .checksum(
                                    ChecksumGenerator.generateBookChecksum(newBookIdentity)
                            )
                            .oldField(
                                    buildBookFieldOption(oldBookIdentity)
                            )
                            .newField(
                                    buildBookFieldOption(newBookIdentity)
                            )
                            .build();
                })
                .flux();
    }

    private BookFieldOption buildBookFieldOption(final BookIdentity bookIdentity) {
        return ImmutableDescriptionBookFieldOption.builder()
                .checksum(ChecksumGenerator.generateBookChecksum(bookIdentity))
                .value(bookIdentity.description())
                .build();
    }

    @Override
    public boolean support(final String field) {
        return FieldOption.DESCRIPTION.name().equals(field);
    }
}
