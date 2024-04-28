package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.out;

import lombok.Data;

@Data
public class BookWithBookIdentityProjection {
    private String isbn;

    private String author;

    private String title;

    private String edition;
}
