package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common;

import com.sokima.reactive.grpc.bookstore.proto.Checksum;
import com.sokima.reactive.grpc.bookstore.proto.ISBN;
import com.sokima.reactive.grpc.bookstore.proto.PurchaseBookResponse;
import com.sokima.reactive.grpc.bookstore.proto.SupplyBookResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public final class GrpcCollectors {

    private GrpcCollectors() {
        throw new UnsupportedOperationException("Instantiation of util class is forbidden.");
    }

    public static Collector<ISBN, List<ISBN>, PurchaseBookResponse> toPurchaseBookResponse() {
        return new IsbnToPurchaseBookResponseCollector();
    }

    public static Collector<ISBN, List<ISBN>, SupplyBookResponse> toSupplyBookResponse(final String checksum) {
        return new IsbnToSupplyBookResponseCollector(checksum);
    }

    private record IsbnToSupplyBookResponseCollector(String checksum) implements Collector<ISBN, List<ISBN>, SupplyBookResponse> {
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
                    .setChecksum(
                            Checksum.newBuilder()
                                    .setValue(checksum)
                                    .build()
                    )
                    .addAllIsbn(isbns)
                    .setQuantity(isbns.size())
                    .build();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Set.of();
        }
    }

    private static class IsbnToPurchaseBookResponseCollector implements Collector<ISBN, List<ISBN>, PurchaseBookResponse> {
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
    }
}
