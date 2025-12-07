package com.carameow.realdatabase;

import com.carameow.realdatabase.domain.dto.AuthorDto;
import com.carameow.realdatabase.domain.dto.BookDto;
import com.carameow.realdatabase.domain.entities.AuthorEntity;
import com.carameow.realdatabase.domain.entities.BookEntity;

public final class TestDataUtil {
    private TestDataUtil() {}

    public static AuthorEntity createTestAuthorEntityA() {
        return AuthorEntity.builder()
                .name("JK Rolling")
                .age(50)
                .build();
    }

    public static AuthorEntity createTestAuthorEntityB() {
        return AuthorEntity.builder()
                .name("George R. R. Martin")
                .age(72)
                .build();
    }

    public static AuthorEntity createTestAuthorEntityC() {
        return AuthorEntity.builder()
                .name("J. R. R. Tolkien")
                .age(81)
                .build();
    }

    public static AuthorDto createTestAuthorDtoA() {
        return AuthorDto.builder()
                .name("JK Rolling")
                .age(50)
                .build();
    }

    public static BookEntity createTestBookEntityA(final AuthorEntity author) {
        return BookEntity.builder()
                .isbn("978-3-16-148410-0")
                .title("Harry Potter")
                .author(author)
                .build();
    }

    public static BookEntity createTestBookEntityB(final AuthorEntity author) {
        return BookEntity.builder()
                .isbn("978-0-553-10354-0")
                .title("A Game of Thrones")
                .author(author)
                .build();
    }

    public static BookEntity createTestBookEntityC(final AuthorEntity author) {
        return BookEntity.builder()
                .isbn("978-0-618-00222-8")
                .title("The Hobbit")
                .author(author)
                .build();
    }

    public static BookDto createTestBookDtoA(final AuthorDto author) {
        return BookDto.builder()
                .isbn("978-3-16-148410-0")
                .title("Harry Potter")
                .author(author)
                .build();
    }
}
