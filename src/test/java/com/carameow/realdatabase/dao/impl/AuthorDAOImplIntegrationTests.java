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
import com.carameow.realdatabase.domain.Author;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorDAOImplIntegrationTests {

    private final AuthorDAOImpl underTest;

    @Autowired
    public AuthorDAOImplIntegrationTests(AuthorDAOImpl underTest) {
        this.underTest = underTest;
    }

    @Test
    public void createAndFindOne_validAuthor_success() {
        Author authorA = TestDataUtil.createTestAuthorA();

        underTest.create(authorA);
        Optional<Author> result = underTest.findOne(authorA.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorA);
    }

    @Test
    public void createAndFind_multipleValidAuthors_success() {
        Author authorA = TestDataUtil.createTestAuthorA();
        Author authorB = TestDataUtil.createTestAuthorB();
        Author authorC = TestDataUtil.createTestAuthorC();

        underTest.create(authorA);
        underTest.create(authorB);
        underTest.create(authorC);
        List<Author> results = underTest.find();

        assertThat(results)
                .hasSize(3)
                .containsExactly(authorA, authorB, authorC);
    }

    @Test
    public void createAndUpdate_validAuthor_success() {
        Author authorA = TestDataUtil.createTestAuthorA();
        underTest.create(authorA);
        authorA.setName("Updated Name");
        underTest.update(authorA.getId(), authorA);

        Optional<Author> result = underTest.findOne(authorA.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorA);
    }

    @Test
    public void createAndDelete_validAuthor_success() {
        Author authorB = TestDataUtil.createTestAuthorB();

        underTest.create(authorB);
        underTest.delete(authorB.getId());
        Optional<Author> result = underTest.findOne(authorB.getId());

        assertThat(result).isEmpty();
    }
}
