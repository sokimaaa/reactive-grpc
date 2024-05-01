package com.sokima.reactive.grpc.bookstore.usecase.update.processor;

import com.sokima.reactive.grpc.bookstore.domain.helper.UpdatableField;
import com.sokima.reactive.grpc.bookstore.domain.port.UpdateBookPort;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.DescriptionUpdateOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.out.UpdateBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.update.processor.mapper.Container2UpdateFlowResultMapper;
import reactor.core.publisher.Flux;

public class DescriptionUpdateOptionProcessor implements UpdateOptionProcessor<DescriptionUpdateOption> {
    private final UpdateBookPort updateBookPort;
    private final Container2UpdateFlowResultMapper containerMapper;

    public DescriptionUpdateOptionProcessor(final UpdateBookPort updateBookPort,
                                            final Container2UpdateFlowResultMapper containerMapper) {
        this.updateBookPort = updateBookPort;
        this.containerMapper = containerMapper;
    }

    @Override
    public Flux<UpdateBookFlowResult> process(final DescriptionUpdateOption bookFieldOption) {
        return updateBookPort.updateBookIdentityField(bookFieldOption.checksum(), bookFieldOption.field(), bookFieldOption.value())
                .map(containerMapper::mapToUpdateFlowResult)
                .flux();
    }

    @Override
    public boolean support(final String field) {
        return UpdatableField.DESCRIPTION.name().equals(field);
    }
}
