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
@Table(name = "book_aggregation")
public class BookAggregationEntity {

    @Id
    @Column("aggregation_id")
    private Long aggregationId;

    @Column("quantity")
    private Long quantity = 0L;

    @Column("checksum")
    private String checksum;
}
