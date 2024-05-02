package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.test.workflow;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Java2ProtoTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Proto2JavaTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.validator.FieldValidator;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.supply.SupplyBookWorkflow;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.test.GrpcAdapterCommonTestContext;
import com.sokima.reactive.grpc.bookstore.infrastructure.context.workflow.SupplyBookWorkflowContext;
import com.sokima.reactive.grpc.bookstore.proto.SupplyBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.SupplyBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.supply.in.SupplyBookInformation;
import com.sokima.reactive.grpc.bookstore.usecase.supply.out.SupplyBookFlowResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({SupplyBookWorkflowContext.class, GrpcAdapterCommonTestContext.class})
@ComponentScan("com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.supply.transformer")
public class SupplyBookWorkflowTestContext {

    @Bean
    SupplyBookWorkflow supplyBookWorkflow(
            final FieldValidator<SupplyBookRequest> requestFieldValidator,
            final Proto2JavaTransformer<SupplyBookRequest, SupplyBookInformation> proto2JavaTransformer,
            final Flow<SupplyBookInformation, SupplyBookFlowResult> flow,
            final Java2ProtoTransformer<SupplyBookResponse, SupplyBookFlowResult> java2ProtoTransformer,
            final FieldValidator<SupplyBookResponse> responseFieldValidator
    ) {
        return new SupplyBookWorkflow(requestFieldValidator, proto2JavaTransformer, flow, java2ProtoTransformer, responseFieldValidator);
    }
}
