package com.carameow.realdatabase.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.carameow.realdatabase.TestDataUtil;
import com.carameow.realdatabase.domain.entities.AuthorEntity;
import com.carameow.realdatabase.domain.entities.BookEntity;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookEntityRepositoryIntegrationTests {

    private final AuthorRepository authorRepo;
    private final BookRepository underTest;

    @Autowired
    public BookEntityRepositoryIntegrationTests(AuthorRepository authorRepo, BookRepository underTest) {
        this.authorRepo = authorRepo;
        this.underTest = underTest;
    }

    @Test
    public void createAndFindOne_validBook_success() {
        AuthorEntity authorA = TestDataUtil.createTestAuthorEntityA();
        authorRepo.save(authorA);
        BookEntity bookA = TestDataUtil.createTestBookEntityA(authorA);

        underTest.save(bookA);

        Optional<BookEntity> result = underTest.findById(bookA.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookA);
    }

    @Test
    public void createAndFind_multipleValidBooksFromOneAuthor_success() {
        AuthorEntity authorA = TestDataUtil.createTestAuthorEntityA();
        authorRepo.save(authorA);

        BookEntity bookA = TestDataUtil.createTestBookEntityA(authorA);
        BookEntity bookB = TestDataUtil.createTestBookEntityB(authorA);
        BookEntity bookC = TestDataUtil.createTestBookEntityC(authorA);
        underTest.saveAll(List.of(bookA, bookB, bookC));

        Iterable<BookEntity> results = underTest.findAll();

        assertThat(results)
                .hasSize(3)
                .containsExactly(bookA, bookB, bookC);
    }

    @Test
    public void createAndFind_multipleValidBooksFromDifferentAuthors_success() {
        AuthorEntity authorA = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity authorB = TestDataUtil.createTestAuthorEntityB();
        authorRepo.saveAll(List.of(authorA, authorB));

        BookEntity bookA = TestDataUtil.createTestBookEntityA(authorA);
        BookEntity bookB = TestDataUtil.createTestBookEntityB(authorB);
        BookEntity bookC = TestDataUtil.createTestBookEntityC(authorB);
        underTest.saveAll(List.of(bookA, bookB, bookC));

        Iterable<BookEntity> results = underTest.findAll();

        assertThat(results)
                .hasSize(3)
                .containsExactly(bookA, bookB, bookC);
    }

    @Test
    public void createAndUpdate_validBook_success() {
        AuthorEntity authorB = TestDataUtil.createTestAuthorEntityB();
        authorRepo.save(authorB);
        BookEntity bookC = TestDataUtil.createTestBookEntityC(authorB);
        underTest.save(bookC);

        bookC.setTitle("Updated title");
        underTest.save(bookC);

        Optional<BookEntity> result = underTest.findById(bookC.getIsbn());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookC);
    }

    @Test
    public void creatAndDelete_validBook_success() {
        AuthorEntity authorC = TestDataUtil.createTestAuthorEntityC();
        authorRepo.save(authorC);

        BookEntity bookB = TestDataUtil.createTestBookEntityB(authorC);
        underTest.save(bookB);

        underTest.deleteById(bookB.getIsbn());
        Optional<BookEntity> result = underTest.findById(bookB.getIsbn());

        assertThat(result).isEmpty();
    }
}
