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
import com.carameow.realdatabase.domain.Author;
import com.carameow.realdatabase.domain.Book;
import com.carameow.realdatabase.repository.AuthorRepository;
import com.carameow.realdatabase.repository.BookRepository;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookRepositoryIntegrationTests {

    private final AuthorRepository authorRepo;
    private final BookRepository underTest;

    @Autowired
    public BookRepositoryIntegrationTests(AuthorRepository authorRepo, BookRepository underTest) {
        this.authorRepo = authorRepo;
        this.underTest = underTest;
    }

    @Test
    public void createAndFindOne_validBook_success() {
        Author authorA = TestDataUtil.createTestAuthorA();
        authorRepo.save(authorA);
        Book bookA = TestDataUtil.createTestBookA(authorA);

        underTest.save(bookA);

        Optional<Book> result = underTest.findById(bookA.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookA);
    }

    @Test
    public void createAndFind_multipleValidBooksFromOneAuthor_success() {
        Author authorA = TestDataUtil.createTestAuthorA();
        authorRepo.save(authorA);

        Book bookA = TestDataUtil.createTestBookA(authorA);
        Book bookB = TestDataUtil.createTestBookB(authorA);
        Book bookC = TestDataUtil.createTestBookC(authorA);
        underTest.saveAll(List.of(bookA, bookB, bookC));

        Iterable<Book> results = underTest.findAll();

        assertThat(results)
                .hasSize(3)
                .containsExactly(bookA, bookB, bookC);
    }

    @Test
    public void createAndFind_multipleValidBooksFromDifferentAuthors_success() {
        Author authorA = TestDataUtil.createTestAuthorA();
        Author authorB = TestDataUtil.createTestAuthorB();
        authorRepo.saveAll(List.of(authorA, authorB));

        Book bookA = TestDataUtil.createTestBookA(authorA);
        Book bookB = TestDataUtil.createTestBookB(authorB);
        Book bookC = TestDataUtil.createTestBookC(authorB);
        underTest.saveAll(List.of(bookA, bookB, bookC));

        Iterable<Book> results = underTest.findAll();

        assertThat(results)
                .hasSize(3)
                .containsExactly(bookA, bookB, bookC);
    }

    @Test
    public void createAndUpdate_validBook_success() {
        Author authorB = TestDataUtil.createTestAuthorB();
        authorRepo.save(authorB);
        Book bookC = TestDataUtil.createTestBookC(authorB);
        underTest.save(bookC);

        bookC.setTitle("Updated title");
        underTest.save(bookC);

        Optional<Book> result = underTest.findById(bookC.getIsbn());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookC);
    }

    @Test
    public void creatAndDelete_validBook_success() {
        Author authorC = TestDataUtil.createTestAuthorC();
        authorRepo.save(authorC);

        Book bookB = TestDataUtil.createTestBookB(authorC);
        underTest.save(bookB);

        underTest.deleteById(bookB.getIsbn());
        Optional<Book> result = underTest.findById(bookB.getIsbn());

        assertThat(result).isEmpty();
    }
}
