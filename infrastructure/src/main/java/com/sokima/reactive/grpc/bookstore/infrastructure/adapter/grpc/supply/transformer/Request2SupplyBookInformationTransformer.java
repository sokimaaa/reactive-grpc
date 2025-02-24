package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.supply.transformer;

import com.sokima.reactive.grpc.bookstore.domain.PartialBookIdentity;
import com.sokima.reactive.grpc.bookstore.domain.helper.BookIdentityMapper;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Proto2JavaTransformer;
import com.sokima.reactive.grpc.bookstore.proto.SupplyBookRequest;
import com.sokima.reactive.grpc.bookstore.usecase.supply.in.ImmutableSupplyBookInformation;
import com.sokima.reactive.grpc.bookstore.usecase.supply.in.SupplyBookInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Request2SupplyBookInformationTransformer extends BookIdentityMapper
        implements Proto2JavaTransformer<SupplyBookRequest, SupplyBookInformation> {

    private static final Logger log = LoggerFactory.getLogger(Request2SupplyBookInformationTransformer.class);

    @Override
    public SupplyBookInformation transform(final SupplyBookRequest proto) {
        log.trace("Transforming to supply book information: {}", proto);
        return ImmutableSupplyBookInformation.builder()
                .supplyNumber(proto.getSupplyNumber())
                .partialBookIdentity((PartialBookIdentity) partialBookIdentity(
                        proto.getTitle(),
                        proto.getAuthor(),
                        proto.getEdition()
                ))
                .build();
    }
}
