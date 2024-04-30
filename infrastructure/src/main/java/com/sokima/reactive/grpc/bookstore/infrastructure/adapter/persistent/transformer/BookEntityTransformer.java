package com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.transformer;

import com.sokima.reactive.grpc.bookstore.domain.Book;
import com.sokima.reactive.grpc.bookstore.domain.BookIdentity;
import com.sokima.reactive.grpc.bookstore.domain.ImmutableBook;
import com.sokima.reactive.grpc.bookstore.domain.generator.IsbnGenerator;
import com.sokima.reactive.grpc.bookstore.domain.helper.CommonTransformer;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.entity.BookAggregationEntity;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.entity.BookEntity;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.entity.BookIdentityEntity;
import com.sokima.reactive.grpc.bookstore.infrastructure.adapter.persistent.repository.out.BookWithBookIdentityProjection;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class BookEntityTransformer {

    private final CommonTransformer commonTransformer;
    private final BookIdentityEntityTransformer bookIdentityEntityTransformer;

    public BookEntityTransformer(
            final CommonTransformer commonTransformer,
            final BookIdentityEntityTransformer bookIdentityEntityTransformer) {
        this.commonTransformer = commonTransformer;
        this.bookIdentityEntityTransformer = bookIdentityEntityTransformer;
    }

    public Book mapToBook(final BookWithBookIdentityProjection projection) {
        return ImmutableBook.builder()
                .isbn(commonTransformer.mapToIsbn(projection.getIsbn()))
                .bookIdentity(bookIdentityEntityTransformer.mapToPartialBookIdentity(projection))
                .build();
    }

    public Book mapToBook(final BookEntity bookEntity, final BookIdentity bookIdentity) {
        return ImmutableBook.builder()
                .isbn(commonTransformer.mapToIsbn(bookEntity.getIsbn()))
                .bookIdentity(bookIdentity)
                .build();
    }

    public Book mapToBook(final BookEntity bookEntity, final BookIdentityEntity bookIdentityEntity) {
        return ImmutableBook.builder()
                .isbn(commonTransformer.mapToIsbn(bookEntity.getIsbn()))
                .bookIdentity(bookIdentityEntityTransformer.mapToBookIdentity(bookIdentityEntity))
                .build();
    }

    public BookEntity copyBookWithPurchased(final BookEntity bookEntity) {
        final var entity = new BookEntity();
        entity.setBookId(bookEntity.getBookId());
        entity.setIsbn(bookEntity.getIsbn());
        entity.setChecksum(bookEntity.getChecksum());
        entity.setIsPurchased(Boolean.TRUE);
        return entity;
    }

    public List<BookEntity> generateBookEntities(final BookAggregationEntity bookAggregation, final Integer count) {
        return Stream.generate(IsbnGenerator::generateIsbn)
                .limit(count)
                .map(isbn -> mapToBookEntity(isbn, bookAggregation.getChecksum()))
                .toList();
    }

    public BookEntity mapToBookEntity(final String isbn, final String checksum) {
        final var bookEntity = new BookEntity();
        bookEntity.setIsbn(isbn);
        bookEntity.setChecksum(checksum);
        return bookEntity;
    }
}
