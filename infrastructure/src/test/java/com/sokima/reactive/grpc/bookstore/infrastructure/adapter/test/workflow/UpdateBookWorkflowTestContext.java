package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.test.workflow;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Java2ProtoTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Proto2JavaTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.validator.FieldValidator;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update.UpdateBookWorkflow;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.test.GrpcAdapterCommonTestContext;
import com.sokima.reactive.grpc.bookstore.infrastructure.context.workflow.UpdateBookWorkflowContext;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.UpdateBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.update.in.UpdateOption;
import com.sokima.reactive.grpc.bookstore.usecase.update.out.UpdateBookFlowResult;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({UpdateBookWorkflowContext.class, GrpcAdapterCommonTestContext.class})
@ComponentScan({
        "com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update.transformer",
        "com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update.option",
        "com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.update.field"
})
public class UpdateBookWorkflowTestContext {

    @Bean
    UpdateBookWorkflow updateBookWorkflow(
            final FieldValidator<UpdateBookRequest> requestFieldValidator,
            final Proto2JavaTransformer<UpdateBookRequest, UpdateOption> proto2JavaTransformer,
            final Flow<UpdateOption, UpdateBookFlowResult> flow,
            final Java2ProtoTransformer<UpdateBookResponse, UpdateBookFlowResult> java2ProtoTransformer,
            final FieldValidator<UpdateBookResponse> responseFieldValidator
    ) {
        return new UpdateBookWorkflow(requestFieldValidator, proto2JavaTransformer, flow, java2ProtoTransformer, responseFieldValidator);
    }
}
