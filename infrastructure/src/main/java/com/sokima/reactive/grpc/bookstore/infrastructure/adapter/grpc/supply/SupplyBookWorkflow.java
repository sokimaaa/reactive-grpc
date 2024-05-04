package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.supply;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.Workflow;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.WorkflowTemplate;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Java2ProtoTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Proto2JavaTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.validator.FieldValidator;
import com.sokima.reactive.grpc.bookstore.proto.SupplyBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.SupplyBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.supply.in.SupplyBookInformation;
import com.sokima.reactive.grpc.bookstore.usecase.supply.out.SupplyBookFlowResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public final class SupplyBookWorkflow implements Workflow<SupplyBookRequest, SupplyBookResponse> {

    private static final Logger log = LoggerFactory.getLogger(SupplyBookWorkflow.class);

    private final WorkflowTemplate<SupplyBookRequest, SupplyBookInformation, SupplyBookFlowResult, SupplyBookResponse> delegate;

    public SupplyBookWorkflow(
            final FieldValidator<SupplyBookRequest> requestFieldValidator,
            final Proto2JavaTransformer<SupplyBookRequest, SupplyBookInformation> proto2JavaTransformer,
            final Flow<SupplyBookInformation, SupplyBookFlowResult> flow,
            final Java2ProtoTransformer<SupplyBookResponse, SupplyBookFlowResult> java2ProtoTransformer,
            final FieldValidator<SupplyBookResponse> responseFieldValidator
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
    public Flux<SupplyBookResponse> doWorkflow(final SupplyBookRequest supplyBookRequest) {
        log.info("Started SupplyBookWorkflow with request: {}", supplyBookRequest);
        return delegate.doWorkflow(supplyBookRequest)
                .doFinally(signalType -> log.info("RegistrationBookWorkflow finished with status: {}", signalType));
    }
}
