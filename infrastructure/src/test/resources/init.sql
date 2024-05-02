CREATE TABLE book_identity
(
    checksum    BPCHAR(256) PRIMARY KEY,
    title       VARCHAR(256) NOT NULL,
    author      VARCHAR(256) NOT NULL,
    edition     VARCHAR(256) NOT NULL,
    description TEXT DEFAULT ('')
);

CREATE TABLE book
(
    book_id      BIGSERIAL PRIMARY KEY,
    isbn         BPCHAR(17) UNIQUE NOT NULL,
    is_purchased BOOLEAN DEFAULT (FALSE),
    checksum     BPCHAR(256) references book_identity (checksum)
);

CREATE TABLE book_aggregation
(
    aggregation_id BIGSERIAL PRIMARY KEY,
    quantity       BIGINT DEFAULT (0),
    checksum       BPCHAR(256) references book_identity (checksum)
);

CREATE INDEX checksum_book_identity_idx ON book_identity USING HASH (checksum);

-- Insert into book_identity
INSERT INTO book_identity (checksum, title, author, edition, description)
VALUES ('c7ad44cbad762a5da0a452f9e8548d17', 'The Great Gatsby', 'F. Scott Fitzgerald', '1st Edition',
        'A novel set in the Jazz Age on Long Island.'),
       ('a7b866a6a397a40b3e41db2cfc3030dc', '1984', 'George Orwell', 'Revised Edition',
        'A dystopian novel set in a totalitarian regime.'),
       ('a7b866a6a397a40b3e41db2cfc3030db', '1984', 'George Orwell', '1st Edition',
        'A dystopian novel set in a totalitarian regime.'),
       ('b2b7c2d5078d709f85f1f957d6269bc9', 'Moby Dick', 'Herman Melville', 'Collector''s Edition',
        'A story of Captain Ahab''s obsession with a giant whale.'),
       ('72c56193fd5aa70c0233951bc62b76b4', 'Pride and Prejudice', 'Jane Austen', '3rd Edition',
        'A romantic novel of manners.'),
       ('7c2496c32d51d874f561c9bf8752467b', 'To Kill a Mockingbird', 'Harper Lee', '2nd Edition',
        'A novel about the serious issues of rape and racial inequality.');

-- Insert into book
INSERT INTO book (isbn, is_purchased, checksum)
VALUES ('9780141182636', TRUE,
        'c7ad44cbad762a5da0a452f9e8548d17'),
       ('9780141182638', FALSE,
        'c7ad44cbad762a5da0a452f9e8548d17'),
       ('9780141182637', FALSE,
        'c7ad44cbad762a5da0a452f9e8548d17'),
       ('9780141182639', FALSE,
        'c7ad44cbad762a5da0a452f9e8548d17'),
       ('9780141182630', FALSE,
        'c7ad44cbad762a5da0a452f9e8548d17'),
       ('9780547249643', FALSE,
        'a7b866a6a397a40b3e41db2cfc3030dc'),
       ('9780547249644', FALSE,
        'a7b866a6a397a40b3e41db2cfc3030dc'),
       ('9780547249645', FALSE,
        'a7b866a6a397a40b3e41db2cfc3030dc'),
       ('9780316769532', FALSE,
        'b2b7c2d5078d709f85f1f957d6269bc9'),
       ('9780316769533', FALSE,
        'b2b7c2d5078d709f85f1f957d6269bc9'),
       ('9780316769534', FALSE,
        'b2b7c2d5078d709f85f1f957d6269bc9'),
       ('9780316769535', FALSE,
        'b2b7c2d5078d709f85f1f957d6269bc9'),
       ('9780316769536', FALSE,
        'b2b7c2d5078d709f85f1f957d6269bc9'),
       ('9780316769537', FALSE,
        'b2b7c2d5078d709f85f1f957d6269bc9'),
       ('9780316769538', FALSE,
        'b2b7c2d5078d709f85f1f957d6269bc9'),
       ('9781853260001', FALSE,
        '72c56193fd5aa70c0233951bc62b76b4'),
       ('9781853260002', FALSE,
        '72c56193fd5aa70c0233951bc62b76b4'),
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
VALUES (5, 'c7ad44cbad762a5da0a452f9e8548d17'),
       (3, 'a7b866a6a397a40b3e41db2cfc3030dc'),
       (7, 'b2b7c2d5078d709f85f1f957d6269bc9'),
       (2, '72c56193fd5aa70c0233951bc62b76b4'),
       (10, '7c2496c32d51d874f561c9bf8752467b');
