package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.test.workflow;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Java2ProtoTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Proto2JavaTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.validator.FieldValidator;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.GetBookWorkflow;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.test.GrpcAdapterCommonTestContext;
import com.sokima.reactive.grpc.bookstore.infrastructure.context.workflow.GetBookWorkflowContext;
import com.sokima.reactive.grpc.bookstore.proto.GetBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.GetBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.get.in.SearchOption;
import com.sokima.reactive.grpc.bookstore.usecase.get.out.GetBookFlowResult;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({GetBookWorkflowContext.class, GrpcAdapterCommonTestContext.class})
@ComponentScan({
        "com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.transformer",
        "com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.get.search"
})
public class GetBookWorkflowTestContext {

    @Bean
    GetBookWorkflow getBookWorkflow(
            final FieldValidator<GetBookRequest> requestFieldValidator,
            final Proto2JavaTransformer<GetBookRequest, SearchOption<?>> proto2JavaTransformer,
            final Flow<SearchOption<?>, GetBookFlowResult> flow,
            final Java2ProtoTransformer<GetBookResponse, GetBookFlowResult> java2ProtoTransformer,
            final FieldValidator<GetBookResponse> responseFieldValidator
    ) {
        return new GetBookWorkflow(
                requestFieldValidator,
                proto2JavaTransformer,
                flow,
                java2ProtoTransformer,
                responseFieldValidator
        );
    }
}
