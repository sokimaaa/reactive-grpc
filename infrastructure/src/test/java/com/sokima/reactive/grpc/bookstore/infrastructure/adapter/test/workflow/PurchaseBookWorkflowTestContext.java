package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.test.workflow;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Java2ProtoTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Proto2JavaTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.validator.FieldValidator;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.purchase.PurchaseBookWorkflow;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.test.GrpcAdapterCommonTestContext;
import com.sokima.reactive.grpc.bookstore.infrastructure.context.workflow.PurchaseBookWorkflowContext;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.in.PurchaseOption;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.out.PurchaseBookFlowResult;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.List;

@TestConfiguration
@Import({PurchaseBookWorkflowContext.class, GrpcAdapterCommonTestContext.class})
@ComponentScan({
        "com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.purchase.transformer",
        "com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.purchase.option"
})
public class PurchaseBookWorkflowTestContext {

    @Bean
    PurchaseBookWorkflow purchaseBookWorkflow(
            final FieldValidator<PurchaseBookRequest> requestFieldValidator,
            final Proto2JavaTransformer<PurchaseBookRequest, PurchaseOption<?>> proto2JavaTransformer,
            final Flow<PurchaseOption<?>, List<PurchaseBookFlowResult>> flow,
            final Java2ProtoTransformer<PurchaseBookResponse, List<PurchaseBookFlowResult>> java2ProtoTransformer,
            final FieldValidator<PurchaseBookResponse> responseFieldValidator
    ) {
        return new PurchaseBookWorkflow(requestFieldValidator, proto2JavaTransformer, flow, java2ProtoTransformer, responseFieldValidator);
    }
}
