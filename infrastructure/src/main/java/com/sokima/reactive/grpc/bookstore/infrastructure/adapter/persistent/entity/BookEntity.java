package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "book")
public class BookEntity implements Persistable<Long> {

    @Id
    @Column("book_id")
    private Long bookId;

    @Version
    @Column("version")
    private Long version = 0L;

    @Column("isbn")
    private String isbn;

    @Column("is_purchased")
    private Boolean isPurchased;

    @Column("checksum")
    private String checksum;

    @Override
    public Long getId() {
        return bookId;
    }

    @Override
    public boolean isNew() {
        return Objects.isNull(bookId) && version == 0L;
    }
}
