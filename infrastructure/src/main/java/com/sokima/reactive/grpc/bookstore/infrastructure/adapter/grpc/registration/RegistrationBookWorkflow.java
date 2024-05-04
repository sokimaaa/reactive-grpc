package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.registration;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.Workflow;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.WorkflowTemplate;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Java2ProtoTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Proto2JavaTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.validator.FieldValidator;
import com.sokima.reactive.grpc.bookstore.proto.RegistrationBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.RegistrationBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.registration.in.RegistrationBookInformation;
import com.sokima.reactive.grpc.bookstore.usecase.registration.out.RegistrationBookFlowResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public final class RegistrationBookWorkflow implements Workflow<RegistrationBookRequest, RegistrationBookResponse> {

    private static final Logger log = LoggerFactory.getLogger(RegistrationBookWorkflow.class);

    private final WorkflowTemplate<RegistrationBookRequest, RegistrationBookInformation, RegistrationBookFlowResult, RegistrationBookResponse> delegate;

    public RegistrationBookWorkflow(
            final FieldValidator<RegistrationBookRequest> requestFieldValidator,
            final Proto2JavaTransformer<RegistrationBookRequest, RegistrationBookInformation> proto2JavaTransformer,
            final Flow<RegistrationBookInformation, RegistrationBookFlowResult> flow,
            final Java2ProtoTransformer<RegistrationBookResponse, RegistrationBookFlowResult> java2ProtoTransformer,
            final FieldValidator<RegistrationBookResponse> responseFieldValidator
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
    public Flux<RegistrationBookResponse> doWorkflow(final RegistrationBookRequest registrationBookRequest) {
        log.info("Started RegistrationBookWorkflow with request: {}", registrationBookRequest);
        return delegate.doWorkflow(registrationBookRequest)
                .doFinally(signalType -> log.info("RegistrationBookWorkflow finished with status: {}", signalType));
    }
}
