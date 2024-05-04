package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.grpc.common.oneof;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

public abstract class AbstractOneofResolver<I, O> implements OneofResolver<I, O> {

    private static final Logger log = LoggerFactory.getLogger(AbstractOneofResolver.class);

    private OneofResolver<I, O> next;

    private void setNext(final OneofResolver<I, O> next) {
        this.next = next;
    }

    public static <I, O> OneofResolver<I, O> createChain(final List<AbstractOneofResolver<I, O>> abstractOneofResolverList) {
        log.trace("Creating chain based on list of resolvers: {}", abstractOneofResolverList);
        for (int i = 0; i < abstractOneofResolverList.size() - 1; i++) {
            abstractOneofResolverList.get(i).setNext(abstractOneofResolverList.get(i + 1));
        }
        return abstractOneofResolverList.get(0);
    }

    @Override
    public O resolve(final I oneof) {
        if (!condition(oneof)) {
            return next().resolve(oneof);
        }
        return result(oneof);
    }

    protected abstract boolean condition(final I oneof);

    protected abstract O result(final I oneof);

    private OneofResolver<I, O> next() {
        return Objects.nonNull(next) ? next : dummyOneofResolver();
    }

    private static <I, O> OneofResolver<I, O> dummyOneofResolver() {
        return oneof -> {
            log.info("Last oneof resolver in chain was invoked. Suspicious oneof: {}", oneof);
            throw new UnsupportedOperationException(format("Any resolvers that could process oneof like: %s", oneof));
        };
    }
}
