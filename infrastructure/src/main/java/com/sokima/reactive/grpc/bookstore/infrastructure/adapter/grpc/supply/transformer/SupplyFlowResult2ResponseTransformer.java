package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.supply.transformer;

import com.sokima.reactive.grpc.bookstore.domain.Isbn;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Java2ProtoTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.ProtoChecksumTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.ProtoIsbnTransformer;
import com.sokima.reactive.grpc.bookstore.proto.ISBN;
import com.sokima.reactive.grpc.bookstore.proto.SupplyBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.supply.out.SupplyBookFlowResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

@Component
public class SupplyFlowResult2ResponseTransformer implements Java2ProtoTransformer<SupplyBookResponse, SupplyBookFlowResult> {

    private final ProtoIsbnTransformer isbnTransformer;
    private final ProtoChecksumTransformer checksumTransformer;

    public SupplyFlowResult2ResponseTransformer(final ProtoIsbnTransformer isbnTransformer,
                                                final ProtoChecksumTransformer checksumTransformer) {
        this.isbnTransformer = isbnTransformer;
        this.checksumTransformer = checksumTransformer;
    }

    @Override
    public SupplyBookResponse transform(final SupplyBookFlowResult pojo) {
        return pojo.isbns()
                .stream()
                .map(Isbn::isbn)
                .map(isbnTransformer::transform)
                .collect(toSupplyBookResponse(pojo.checksum()));
    }

    private Collector<ISBN, List<ISBN>, SupplyBookResponse> toSupplyBookResponse(final String checksum) {
        return new Collector<>() {
            @Override
            public Supplier<List<ISBN>> supplier() {
                return ArrayList::new;
            }

            @Override
            public BiConsumer<List<ISBN>, ISBN> accumulator() {
                return List::add;
            }

            @Override
            public BinaryOperator<List<ISBN>> combiner() {
                return (l, r) -> {
                    l.addAll(r);
                    return l;
                };
            }

            @Override
            public Function<List<ISBN>, SupplyBookResponse> finisher() {
                return isbns -> SupplyBookResponse.newBuilder()
                        .setChecksum(checksumTransformer.transform(checksum))
                        .addAllIsbn(isbns)
                        .setQuantity(isbns.size())
                        .build();
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Set.of();
            }
        };
    }
}
