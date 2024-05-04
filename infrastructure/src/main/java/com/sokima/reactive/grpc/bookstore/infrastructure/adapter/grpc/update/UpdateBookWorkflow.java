package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.Workflow;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.WorkflowTemplate;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Java2ProtoTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Proto2JavaTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.validator.FieldValidator;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.UpdateOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.out.UpdateBookFlowResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public final class UpdateBookWorkflow implements Workflow<UpdateBookRequest, UpdateBookResponse> {

    private static final Logger log = LoggerFactory.getLogger(UpdateBookWorkflow.class);

    private final WorkflowTemplate<UpdateBookRequest, UpdateOption, UpdateBookFlowResult, UpdateBookResponse> delegate;

    public UpdateBookWorkflow(
            final FieldValidator<UpdateBookRequest> requestFieldValidator,
            final Proto2JavaTransformer<UpdateBookRequest, UpdateOption> proto2JavaTransformer,
            final Flow<UpdateOption, UpdateBookFlowResult> flow,
            final Java2ProtoTransformer<UpdateBookResponse, UpdateBookFlowResult> java2ProtoTransformer,
            final FieldValidator<UpdateBookResponse> responseFieldValidator
    ) {
        this.delegate = new WorkflowTemplate<>(
                requestFieldValidator,
                proto2JavaTransformer,
                flow,
                java2ProtoTransformer,
                responseFieldValidator
        );
    }

    @Override
    public Flux<UpdateBookResponse> doWorkflow(final UpdateBookRequest updateBookRequest) {
        log.info("Started UpdateBookWorkflow with request: {}", updateBookRequest);
        return delegate.doWorkflow(updateBookRequest)
                .doFinally(signalType -> log.info("UpdateBookWorkflow finished with status: {}", signalType));
    }
}
