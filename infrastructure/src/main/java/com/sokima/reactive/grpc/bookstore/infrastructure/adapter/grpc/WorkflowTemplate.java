package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc;

import com.google.protobuf.Message;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Java2ProtoTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Proto2JavaTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.validator.FieldValidator;
import com.sokima.reactive.grpc.bookstore.usecase.Flow;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public final class WorkflowTemplate<INBOUND extends Message, INTERMEDIATE_INBOUND, INTERMEDIATE_OUTBOUND, OUTBOUND extends Message>
        implements Workflow<INBOUND, OUTBOUND> {

    private final FieldValidator<INBOUND> inboundFieldValidator;
    private final Java2ProtoTransformer<OUTBOUND, INTERMEDIATE_OUTBOUND> java2ProtoTransformer;
    private final Flow<INTERMEDIATE_INBOUND, INTERMEDIATE_OUTBOUND> flow;
    private final Proto2JavaTransformer<INBOUND, INTERMEDIATE_INBOUND> proto2JavaTransformer;
    private final FieldValidator<OUTBOUND> outboundFieldValidator;

    public WorkflowTemplate(
            final FieldValidator<INBOUND> inboundFieldValidator,
            final Proto2JavaTransformer<INBOUND, INTERMEDIATE_INBOUND> proto2JavaTransformer,
            final Flow<INTERMEDIATE_INBOUND, INTERMEDIATE_OUTBOUND> flow,
            final Java2ProtoTransformer<OUTBOUND, INTERMEDIATE_OUTBOUND> java2ProtoTransformer,
            final FieldValidator<OUTBOUND> outboundFieldValidator) {
        this.inboundFieldValidator = inboundFieldValidator;
        this.java2ProtoTransformer = java2ProtoTransformer;
        this.flow = flow;
        this.proto2JavaTransformer = proto2JavaTransformer;
        this.outboundFieldValidator = outboundFieldValidator;
    }

    @Override
    public Flux<OUTBOUND> doWorkflow(final INBOUND inbound) {
        return Mono.just(inbound)
                .map(inboundFieldValidator)
                .map(proto2JavaTransformer::transform)
                .flatMapMany(flow::doFlow)
                .map(java2ProtoTransformer::transform)
                .map(outboundFieldValidator);
    }
}
