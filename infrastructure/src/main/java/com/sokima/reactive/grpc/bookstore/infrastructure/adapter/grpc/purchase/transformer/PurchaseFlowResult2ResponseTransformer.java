package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.purchase.transformer;

import com.sokima.reactive.grpc.bookstore.domain.Isbn;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.Java2ProtoTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.transformer.ProtoIsbnTransformer;
import com.sokima.reactive.grpc.bookstore.proto.ISBN;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookResponse;
import com.sokima.reactive.grpc.bookstore.usecase.purchase.out.PurchaseBookFlowResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class PurchaseFlowResult2ResponseTransformer implements Java2ProtoTransformer<PurchaseBookResponse, List<PurchaseBookFlowResult>> {

    private static final Logger log = LoggerFactory.getLogger(PurchaseFlowResult2ResponseTransformer.class);

    private final ProtoIsbnTransformer isbnTransformer;

    public PurchaseFlowResult2ResponseTransformer(final ProtoIsbnTransformer isbnTransformer) {
        this.isbnTransformer = isbnTransformer;
    }

    @Override
    public PurchaseBookResponse transform(final List<PurchaseBookFlowResult> pojo) {
        log.trace("Transforming to proto purchase book response: {}", pojo);
        return pojo.stream()
                .map(PurchaseBookFlowResult::purchasedIsbn)
                .map(Isbn::isbn)
                .map(isbnTransformer::transform)
                .collect(toPurchaseBookResponse());
    }

    private Collector<ISBN, List<ISBN>, PurchaseBookResponse> toPurchaseBookResponse() {
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
            public Function<List<ISBN>, PurchaseBookResponse> finisher() {
                return isbns -> PurchaseBookResponse.newBuilder()
                        .addAllPurchasedBooks(isbns)
                        .build();
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Set.of();
            }
        };
    }
}
