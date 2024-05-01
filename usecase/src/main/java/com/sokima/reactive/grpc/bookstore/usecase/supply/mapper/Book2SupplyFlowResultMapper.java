package com.sokima.reactive.grpc.bookstore.usecase.supply.mapper;

import com.sokima.reactive.grpc.bookstore.domain.Book;
import com.sokima.reactive.grpc.bookstore.domain.generator.ChecksumGenerator;
import com.sokima.reactive.grpc.bookstore.usecase.supply.out.ImmutableSupplyBookFlowResult;
import com.sokima.reactive.grpc.bookstore.usecase.supply.out.SupplyBookFlowResult;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class Book2SupplyFlowResultMapper {
    public SupplyBookFlowResult mapToSupplyFlowResult(final List<Book> bookList) {
        return ImmutableSupplyBookFlowResult.builder()
                .isbns(bookList.stream().map(Book::isbn).toList())
                .checksum(
                        bookList.stream().findFirst().map(Book::bookIdentity)
                                .map(ChecksumGenerator::generateBookChecksum)
                                .orElse(StringUtils.EMPTY)
                )
                .build();
    }
}
