package com.carameow.realdatabase;

import com.carameow.realdatabase.domain.Author;
import com.carameow.realdatabase.domain.Book;

public final class TestDataUtil {
    private TestDataUtil() {}

    public static Author createTestAuthorA() {
        return Author.builder()
                .id(1L)
                .name("JK Rolling")
                .age(50)
                .build();
    }

    public static Author createTestAuthorB() {
        return Author.builder()
                .id(2L)
                .name("George R. R. Martin")
                .age(72)
                .build();
    }

    public static Author createTestAuthorC() {
        return Author.builder()
                .id(3L)
                .name("J. R. R. Tolkien")
                .age(81)
                .build();
    }

    public static Book createTestBookA() {
        return Book.builder()
                .isbn("978-3-16-148410-0")
                .title("Harry Potter")
                .authorId(1L)
                .build();
    }

    public static Book createTestBookB() {
        return Book.builder()
                .isbn("978-0-553-10354-0")
                .title("A Game of Thrones")
                .authorId(2L)
                .build();
    }

    public static Book createTestBookC() {
        return Book.builder()
                .isbn("978-0-618-00222-8")
                .title("The Hobbit")
                .authorId(3L)
                .build();
    }
}
