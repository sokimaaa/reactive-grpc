package com.sokima.reactive.grpc.bookstore.domain.oneof;

import java.util.Objects;

public abstract class AbstractOneofResolver<I, O> implements OneofResolver<I, O> {

    protected OneofResolver<I, O> next;

    public void setNext(final OneofResolver<I, O> next) {
        this.next = next;
    }

    @Override
    public OneofResolver<I, O> next() {
        return Objects.nonNull(next) ? next : OneofResolver.dummyOneofResolver();
    }
}
