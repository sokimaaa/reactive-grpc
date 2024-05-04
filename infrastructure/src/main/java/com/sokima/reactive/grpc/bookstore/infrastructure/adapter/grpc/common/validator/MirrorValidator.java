package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.validator;

import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class MirrorValidator<M extends Message> implements FieldValidator<M> {

    private static final Logger log = LoggerFactory.getLogger(MirrorValidator.class);

    @Override
    public M validate(final M message) {
        log.debug("Mirror validator invoked for proto message: {}", message);
        return message;
    }
}
