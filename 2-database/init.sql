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

-- Insert into book_identity
INSERT INTO book_identity (checksum, title, author, edition, description)
VALUES ('d3b9af9e8a72cc6d0725924a4bd1cc5f', 'The Great Gatsby', 'F. Scott Fitzgerald', '1st Edition',
        'A novel set in the Jazz Age on Long Island.'),
       ('1de9dd259a5aea8e1d0e710e29774069', '1984', 'George Orwell', 'Revised Edition',
        'A dystopian novel set in a totalitarian regime.'),
       ('bb249594cb35ecbe36872cfc8f11a2a0', '1984', 'George Orwell', '1st Edition',
        'A dystopian novel set in a totalitarian regime.'),
       ('6f4589c94a41ec1ead930c6efd3b80f2', 'Moby Dick', 'Herman Melville', 'Collectors Edition',
        'A story of Captain Ahab''s obsession with a giant whale.'),
       ('52e2c3b92ed9e4e01f94f2560bf40c5b', 'Pride and Prejudice', 'Jane Austen', '3rd Edition',
        'A romantic novel of manners.'),
       ('7c2496c32d51d874f561c9bf8752467b', 'To Kill a Mockingbird', 'Harper Lee', '2nd Edition',
        'A novel about the serious issues of rape and racial inequality.');

-- Insert into book
INSERT INTO book (isbn, is_purchased, checksum)
VALUES ('9780141182636', TRUE,
        'd3b9af9e8a72cc6d0725924a4bd1cc5f'),
       ('9780141182638', FALSE,
        'd3b9af9e8a72cc6d0725924a4bd1cc5f'),
       ('9780141182637', FALSE,
        'd3b9af9e8a72cc6d0725924a4bd1cc5f'),
       ('9780141182639', FALSE,
        'd3b9af9e8a72cc6d0725924a4bd1cc5f'),
       ('9780141182630', FALSE,
        'd3b9af9e8a72cc6d0725924a4bd1cc5f'),
       ('9780547249643', FALSE,
        '1de9dd259a5aea8e1d0e710e29774069'),
       ('9780547249644', FALSE,
        '1de9dd259a5aea8e1d0e710e29774069'),
       ('9780547249645', FALSE,
        '1de9dd259a5aea8e1d0e710e29774069'),
       ('9780316769532', FALSE,
        '6f4589c94a41ec1ead930c6efd3b80f2'),
       ('9780316769533', FALSE,
        '6f4589c94a41ec1ead930c6efd3b80f2'),
       ('9780316769534', FALSE,
        '6f4589c94a41ec1ead930c6efd3b80f2'),
       ('9780316769535', FALSE,
        '6f4589c94a41ec1ead930c6efd3b80f2'),
       ('9780316769536', FALSE,
        '6f4589c94a41ec1ead930c6efd3b80f2'),
       ('9780316769537', FALSE,
        '6f4589c94a41ec1ead930c6efd3b80f2'),
       ('9780316769538', FALSE,
        '6f4589c94a41ec1ead930c6efd3b80f2'),
       ('9781853260001', FALSE,
        '52e2c3b92ed9e4e01f94f2560bf40c5b'),
       ('9781853260002', FALSE,
        '52e2c3b92ed9e4e01f94f2560bf40c5b'),
       ('9780061120084', FALSE,
        '7c2496c32d51d874f561c9bf8752467b'),
       ('9780061120085', FALSE,
        '7c2496c32d51d874f561c9bf8752467b'),
       ('9780061120086', FALSE,
        '7c2496c32d51d874f561c9bf8752467b'),
       ('9780061120087', FALSE,
        '7c2496c32d51d874f561c9bf8752467b'),
       ('9780061120088', FALSE,
        '7c2496c32d51d874f561c9bf8752467b'),
       ('9780061120089', FALSE,
        '7c2496c32d51d874f561c9bf8752467b'),
       ('9780061120090', FALSE,
        '7c2496c32d51d874f561c9bf8752467b'),
       ('9780061120091', FALSE,
        '7c2496c32d51d874f561c9bf8752467b'),
       ('9780061120092', FALSE,
        '7c2496c32d51d874f561c9bf8752467b'),
       ('9780061120093', FALSE,
        '7c2496c32d51d874f561c9bf8752467b');

-- Insert into book_aggregation
INSERT INTO book_aggregation (quantity, checksum)
VALUES (5, 'd3b9af9e8a72cc6d0725924a4bd1cc5f'),
       (3, '1de9dd259a5aea8e1d0e710e29774069'),
       (7, '6f4589c94a41ec1ead930c6efd3b80f2'),
       (2, '52e2c3b92ed9e4e01f94f2560bf40c5b'),
       (10, '7c2496c32d51d874f561c9bf8752467b'),
       (0, 'bb249594cb35ecbe36872cfc8f11a2a0');
