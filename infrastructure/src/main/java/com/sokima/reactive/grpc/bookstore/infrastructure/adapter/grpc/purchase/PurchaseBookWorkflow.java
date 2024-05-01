package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.purchase;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Java2ProtoTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Proto2JavaTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.validator.FieldValidator;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.Workflow;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.WorkflowTemplate;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.PurchaseOption;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.out.PurchaseBookFlowResult;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
public final class PurchaseBookWorkflow implements Workflow<PurchaseBookRequest, PurchaseBookResponse> {

    private final WorkflowTemplate<PurchaseBookRequest, PurchaseOption<?>, List<PurchaseBookFlowResult>, PurchaseBookResponse> delegate;

    public PurchaseBookWorkflow(
            final FieldValidator<PurchaseBookRequest> requestFieldValidator,
            final Proto2JavaTransformer<PurchaseBookRequest, PurchaseOption<?>> proto2JavaTransformer,
            final Flow<PurchaseOption<?>, List<PurchaseBookFlowResult>> flow,
            final Java2ProtoTransformer<PurchaseBookResponse, List<PurchaseBookFlowResult>> java2ProtoTransformer,
            final FieldValidator<PurchaseBookResponse> responseFieldValidator
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
    public Flux<PurchaseBookResponse> doWorkflow(final PurchaseBookRequest purchaseBookRequest) {
        return delegate.doWorkflow(purchaseBookRequest);
    }
}
