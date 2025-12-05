package com.carameow.realdatabase.dao.impl;

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
import com.carameow.realdatabase.dao.AuthorDAO;
import com.carameow.realdatabase.domain.Author;
import com.carameow.realdatabase.domain.Book;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookDAOImplIntegrationTests {

    private AuthorDAO authorDAO;
    private BookDAOImpl underTest;

    @Autowired
    public BookDAOImplIntegrationTests(AuthorDAO authorDAO, BookDAOImpl underTest) {
        this.authorDAO = authorDAO;
        this.underTest = underTest;
    }

    @Test
    public void createAndFindOne_validBook_success() {
        Author authorA = TestDataUtil.createTestAuthorA();
        authorDAO.create(authorA);

        Book bookA = TestDataUtil.createTestBookA();
        bookA.setAuthorId(authorA.getId());

        underTest.create(bookA);
        Optional<Book> result = underTest.findOne(bookA.getIsbn());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(bookA);
    }

    @Test
    public void createAndFind_multipleValidBooksFromOneAuthor_success() {
        Author authorA = TestDataUtil.createTestAuthorA();
        authorDAO.create(authorA);

        Book bookA = TestDataUtil.createTestBookA();
        Book bookB = TestDataUtil.createTestBookB();
        Book bookC = TestDataUtil.createTestBookC();
        bookA.setAuthorId(authorA.getId());
        bookB.setAuthorId(authorA.getId());
        bookC.setAuthorId(authorA.getId());

        underTest.create(bookA);
        underTest.create(bookB);
        underTest.create(bookC);
        List<Book> results = underTest.find();

        assertThat(results)
                .hasSize(3)
                .containsExactly(bookA, bookB, bookC);
    }

    @Test
    public void createAndFind_multipleValidBooksFromDifferentAuthors_success() {
        Author authorA = TestDataUtil.createTestAuthorA();
        Author authorB = TestDataUtil.createTestAuthorB();
        authorDAO.create(authorA);
        authorDAO.create(authorB);

        Book bookA = TestDataUtil.createTestBookA();
        bookA.setAuthorId(authorA.getId());
        Book bookB = TestDataUtil.createTestBookB();
        Book bookC = TestDataUtil.createTestBookC();
        bookB.setAuthorId(authorB.getId());
        bookC.setAuthorId(authorB.getId());

        underTest.create(bookA);
        underTest.create(bookB);
        underTest.create(bookC);
        List<Book> results = underTest.find();

        assertThat(results)
                .hasSize(3)
                .containsExactly(bookA, bookB, bookC);
    }

    @Test
    public void createAndUpdate_validBook_success() {
        Author authorB = TestDataUtil.createTestAuthorB();
        authorDAO.create(authorB);

        Book bookC = TestDataUtil.createTestBookC();
        bookC.setAuthorId(authorB.getId());
        underTest.create(bookC);
        bookC.setTitle("Updated title");
        underTest.update(bookC.getIsbn(), bookC);

        Optional<Book> result = underTest.findOne(bookC.getIsbn());
        assertThat(result.get()).isEqualTo(bookC);
    }

    @Test
    public void creatAndDelete_validBook_success() {
        Author authorC = TestDataUtil.createTestAuthorC();
        authorDAO.create(authorC);

        Book bookB = TestDataUtil.createTestBookB();
        bookB.setAuthorId(authorC.getId());
        underTest.create(bookB);

        underTest.delete(bookB.getIsbn());
        Optional<Book> result = underTest.findOne(bookB.getIsbn());

        assertThat(result).isEmpty();
    }
}
