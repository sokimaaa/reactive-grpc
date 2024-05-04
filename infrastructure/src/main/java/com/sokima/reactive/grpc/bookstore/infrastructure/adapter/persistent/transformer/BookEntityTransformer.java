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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class BookEntityTransformer {

    private static final Logger log = LoggerFactory.getLogger(BookEntityTransformer.class);

    private final CommonTransformer commonTransformer;
    private final BookIdentityEntityTransformer bookIdentityEntityTransformer;

    public BookEntityTransformer(
            final CommonTransformer commonTransformer,
            final BookIdentityEntityTransformer bookIdentityEntityTransformer) {
        this.commonTransformer = commonTransformer;
        this.bookIdentityEntityTransformer = bookIdentityEntityTransformer;
    }

    public Book mapToBook(final BookWithBookIdentityProjection projection) {
        log.trace("Transforming to book: {}", projection);
        return ImmutableBook.builder()
                .isbn(commonTransformer.mapToIsbn(projection.getIsbn()))
                .bookIdentity(bookIdentityEntityTransformer.mapToPartialBookIdentity(projection))
                .build();
    }

    public Book mapToBook(final BookEntity bookEntity, final BookIdentity bookIdentity) {
        log.trace("Transforming to book: {} with book identity: {}", bookEntity, bookIdentity);
        return ImmutableBook.builder()
                .isbn(commonTransformer.mapToIsbn(bookEntity.getIsbn()))
                .bookIdentity(bookIdentity)
                .build();
    }

    public Book mapToBook(final BookEntity bookEntity, final BookIdentityEntity bookIdentityEntity) {
        log.trace("Transforming to book: {} with book identity entity: {}", bookEntity, bookIdentityEntity);
        return ImmutableBook.builder()
                .isbn(commonTransformer.mapToIsbn(bookEntity.getIsbn()))
                .bookIdentity(bookIdentityEntityTransformer.mapToBookIdentity(bookIdentityEntity))
                .build();
    }

    public BookEntity copyBookWithPurchased(final BookEntity bookEntity) {
        log.trace("Transforming to book entity: {}", bookEntity);
        final var entity = new BookEntity();
        entity.setBookId(bookEntity.getBookId());
        entity.setIsbn(bookEntity.getIsbn());
        entity.setChecksum(bookEntity.getChecksum());
        entity.setIsPurchased(Boolean.TRUE);
        return entity;
    }

    public List<BookEntity> generateBookEntities(final BookAggregationEntity bookAggregation, final Integer count) {
        log.trace("Transforming to book entities: {} with count: {}", bookAggregation, count);
        return Stream.generate(IsbnGenerator::generateIsbn)
                .limit(count)
                .map(isbn -> mapToBookEntity(isbn, bookAggregation.getChecksum()))
                .toList();
    }

    public BookEntity mapToBookEntity(final String isbn, final String checksum) {
        log.trace("Transforming to book entity: {}, {}", isbn, checksum);
        final var bookEntity = new BookEntity();
        bookEntity.setIsbn(isbn);
        bookEntity.setChecksum(checksum);
        return bookEntity;
    }
}
