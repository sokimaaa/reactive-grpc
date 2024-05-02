package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.test.workflow;

import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Java2ProtoTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Proto2JavaTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.validator.FieldValidator;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.registration.RegistrationBookWorkflow;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.test.GrpcAdapterCommonTestContext;
import com.sokima.reactive.grpc.bookstore.infrastructure.context.workflow.RegistrationBookWorkflowContext;
import com.sokima.reactive.grpc.bookstore.proto.RegistrationBookRequest;
import com.sokima.reactive.grpc.bookstore.proto.RegistrationBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import com.sokima.reactive.grpc.bookstore.usecase.registration.in.RegistrationBookInformation;
import com.sokima.reactive.grpc.bookstore.usecase.registration.out.RegistrationBookFlowResult;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({RegistrationBookWorkflowContext.class, GrpcAdapterCommonTestContext.class})
@ComponentScan("com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.registration.transformer")
public class RegistrationBookWorkflowTestContext {

    @Bean
    RegistrationBookWorkflow registrationBookWorkflow(
            final FieldValidator<RegistrationBookRequest> requestFieldValidator,
            final Proto2JavaTransformer<RegistrationBookRequest, RegistrationBookInformation> proto2JavaTransformer,
            final Flow<RegistrationBookInformation, RegistrationBookFlowResult> flow,
            final Java2ProtoTransformer<RegistrationBookResponse, RegistrationBookFlowResult> java2ProtoTransformer,
            final FieldValidator<RegistrationBookResponse> responseFieldValidator
    ) {
        return new RegistrationBookWorkflow(requestFieldValidator, proto2JavaTransformer, flow, java2ProtoTransformer, responseFieldValidator);
    }
}
