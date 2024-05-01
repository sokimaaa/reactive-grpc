package com.sokima.reactive.grpc.bookstore.domain.oneof;

import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

@Deprecated
public abstract class AbstractOneofResolver<I, O> implements OneofResolver<I, O> {

    private OneofResolver<I, O> next;

    private void setNext(final OneofResolver<I, O> next) {
        this.next = next;
    }

    public static <I, O> OneofResolver<I, O> createChain(final List<AbstractOneofResolver<I, O>> abstractOneofResolverList) {
        for (int i = 0; i < abstractOneofResolverList.size() - 1; i++) {
            abstractOneofResolverList.get(i).setNext(abstractOneofResolverList.get(i + 1));
        }
        return abstractOneofResolverList.get(0);
    }

    protected OneofResolver<I, O> next() {
        return Objects.nonNull(next) ? next : dummyOneofResolver();
    }

    private static <I, O> OneofResolver<I, O> dummyOneofResolver() {
        return oneof -> {
            throw new UnsupportedOperationException(format("Any resolvers that could process oneof like: %s", oneof));
        };
    }
}
