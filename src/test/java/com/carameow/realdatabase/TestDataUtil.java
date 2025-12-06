package com.carameow.realdatabase;

import com.carameow.realdatabase.domain.Author;
import com.carameow.realdatabase.domain.Book;

public final class TestDataUtil {
    private TestDataUtil() {}

    public static Author createTestAuthorA() {
        return Author.builder()
                .name("JK Rolling")
                .age(50)
                .build();
    }

    public static Author createTestAuthorB() {
        return Author.builder()
                .name("George R. R. Martin")
                .age(72)
                .build();
    }

    public static Author createTestAuthorC() {
        return Author.builder()
                .name("J. R. R. Tolkien")
                .age(81)
                .build();
    }

    public static Book createTestBookA(final Author author) {
        return Book.builder()
                .isbn("978-3-16-148410-0")
                .title("Harry Potter")
                .author(author)
                .build();
    }

    public static Book createTestBookB(final Author author) {
        return Book.builder()
                .isbn("978-0-553-10354-0")
                .title("A Game of Thrones")
                .author(author)
                .build();
    }

    public static Book createTestBookC(final Author author) {
        return Book.builder()
                .isbn("978-0-618-00222-8")
                .title("The Hobbit")
                .author(author)
                .build();
    }
}
