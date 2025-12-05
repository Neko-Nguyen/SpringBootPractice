DROP TABLE IF EXISTS "author";
DROP TABLE IF EXISTS "book";

DROP SEQUENCE IF EXISTS author_id_seq;
CREATE SEQUENCE author_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 47328432748923 CACHE 1;

CREATE TABLE "author" (
    "id" bigint DEFAULT nextval('author_id_seq') NOT NULL,
    "name" text,
    "age" integer,
    CONSTRAINT "author_pkey" PRIMARY KEY ("id")

);

CREATE TABLE "book" (
    "isbn" text NOT NULL,
    "title" text,
    "author_id" bigint,
    CONSTRAINT "book_pkey" PRIMARY KEY ("isbn"),
    CONSTRAINT "fk_author" FOREIGN KEY (author_id) REFERENCES author(id)
);