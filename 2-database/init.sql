DROP TABLE IF EXISTS book_identity;
CREATE TABLE book_identity
(
    checksum    BPCHAR(256) PRIMARY KEY,
    title       VARCHAR(256) NOT NULL,
    author      VARCHAR(256) NOT NULL,
    edition     VARCHAR(256) NOT NULL,
    description TEXT DEFAULT ('')
);

DROP TABLE IF EXISTS book;
CREATE TABLE book
(
    book_id      BIGSERIAL PRIMARY KEY,
    isbn         BPCHAR(17) UNIQUE NOT NULL,
    is_purchased BOOLEAN DEFAULT (FALSE),
    checksum     BPCHAR(256) references book_identity (checksum)
);

DROP TABLE IF EXISTS book_aggregation;
CREATE TABLE book_aggregation
(
    aggregation_id BIGSERIAL PRIMARY KEY,
    quantity       BIGINT DEFAULT (0),
    checksum       BPCHAR(256) references book_identity (checksum)
);

DROP INDEX IF EXISTS checksum_book_identity_idx;
CREATE INDEX checksum_book_identity_idx ON book_identity USING HASH (checksum);
