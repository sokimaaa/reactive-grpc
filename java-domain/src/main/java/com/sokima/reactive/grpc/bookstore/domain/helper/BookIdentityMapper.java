package com.sokima.reactive.grpc.bookstore.domain.helper;

import com.sokima.reactive.grpc.bookstore.domain.BookIdentity;
import com.sokima.reactive.grpc.bookstore.domain.ImmutableBookIdentity;
import com.sokima.reactive.grpc.bookstore.domain.ImmutablePartialBookIdentity;

public abstract class BookIdentityMapper {

    protected BookIdentity bookIdentity(
            final String title, final String author, final String edition, final String description
    ) {
        return ImmutableBookIdentity.builder()
                .title(title)
                .author(author)
                .edition(edition)
                .description(description)
                .build();
    }

    protected BookIdentity partialBookIdentity(
            final String title, final String author, final String edition
    ) {
        return ImmutablePartialBookIdentity.builder()
                .author(author)
                .edition(edition)
                .title(title)
                .build();
    }
}
