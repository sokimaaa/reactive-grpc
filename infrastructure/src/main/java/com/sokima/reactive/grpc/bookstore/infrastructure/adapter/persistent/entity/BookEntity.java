package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "book")
public class BookEntity {

    @Id
    @Column("book_id")
    private Long bookId;

    @Column("isbn")
    private String isbn;

    @Column("is_purchased")
    private Boolean isPurchased;

    @Column("checksum")
    private String checksum;
}
