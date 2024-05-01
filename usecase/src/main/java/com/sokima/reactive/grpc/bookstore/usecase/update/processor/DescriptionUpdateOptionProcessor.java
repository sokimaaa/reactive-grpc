package com.sokima.reactive.grpc.bookstore.usecase.update.processor;

import com.sokima.reactive.grpc.bookstore.domain.BookIdentity;
import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.domain.helper.FieldOption;
import com.sokima.reactive.grpc.bookstore.domain.port.UpdateBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.ImmutableDescriptionUpdateOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.UpdateOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.DescriptionUpdateOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.out.ImmutableUpdateBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.update.out.UpdateBookFlowResult;
import reactor.core.publisher.Flux;

public class DescriptionUpdateOptionProcessor implements UpdateOptionProcessor<DescriptionUpdateOption> {
    private final UpdateBookPort updateBookPort;

    public DescriptionUpdateOptionProcessor(final UpdateBookPort updateBookPort) {
        this.updateBookPort = updateBookPort;
    }

    @Override
    public Flux<UpdateBookFlowResult> process(final DescriptionUpdateOption bookFieldOption) {
        return updateBookPort.updateBookIdentityField(bookFieldOption.checksum(), bookFieldOption.field(), bookFieldOption.value())
                .<UpdateBookFlowResult>map(bookIdentityContainer -> {
                    final BookIdentity oldBookIdentity = bookIdentityContainer.oldDomainObject();
                    final BookIdentity newBookIdentity = bookIdentityContainer.newDomainObject();
                    return ImmutableUpdateBookFlowResult.builder()
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

    private UpdateOption buildBookFieldOption(final BookIdentity bookIdentity) {
        return ImmutableDescriptionUpdateOption.builder()
                .checksum(ChecksumGenerator.generateBookChecksum(bookIdentity))
                .value(bookIdentity.description())
                .build();
    }

    @Override
    public boolean support(final String field) {
        return FieldOption.DESCRIPTION.name().equals(field);
    }
}
