package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "book_identity")
public class BookIdentityEntity implements Persistable<String> {

    @Id
    @Column("checksum")
    private String checksum;

    @Column("title")
    private String title;

    @Column("author")
    private String author;

    @Column("edition")
    private String edition;

    @Column("description")
    private String description;

    @Transient
    private Boolean isNew = Boolean.FALSE;

    @Override
    public String getId() {
        return checksum;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
