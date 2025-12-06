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
import com.carameow.realdatabase.repository.AuthorRepository;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorRepositoryIntegrationTests {

    private final AuthorRepository underTest;

    @Autowired
    public AuthorRepositoryIntegrationTests(AuthorRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void createAndFindOne_validAuthor_success() {
        Author authorA = TestDataUtil.createTestAuthorA();
        underTest.save(authorA);

        Optional<Author> result = underTest.findById(authorA.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorA);
    }

    @Test
    public void createAndFind_multipleValidAuthors_success() {
        Author authorA = TestDataUtil.createTestAuthorA();
        Author authorB = TestDataUtil.createTestAuthorB();
        Author authorC = TestDataUtil.createTestAuthorC();
        underTest.saveAll(List.of(authorA, authorB, authorC));

        Iterable<Author> results = underTest.findAll();

        assertThat(results)
                .hasSize(3)
                .containsExactly(authorA, authorB, authorC);
    }

    @Test
    public void createAndUpdate_validAuthor_success() {
        Author authorA = TestDataUtil.createTestAuthorA();
        underTest.save(authorA);

        authorA.setName("Updated Name");
        underTest.save(authorA);

        Optional<Author> result = underTest.findById(authorA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorA);
    }

    @Test
    public void createAndDelete_validAuthor_success() {
        Author authorB = TestDataUtil.createTestAuthorB();
        underTest.save(authorB);

        underTest.deleteById(authorB.getId());
        Optional<Author> result = underTest.findById(authorB.getId());

        assertThat(result).isEmpty();
    }

    @Test
    public void getAuthorWithAgeLessThan_validAge_success() {
        Author authorA = TestDataUtil.createTestAuthorA(); // age 50
        Author authorB = TestDataUtil.createTestAuthorB(); // age 72
        Author authorC = TestDataUtil.createTestAuthorC(); // age 81
        underTest.saveAll(List.of(authorA, authorB, authorC));

        Iterable<Author> result = underTest.ageLessThan(75);

        assertThat(result).containsExactly(authorA, authorB);
    }

    @Test
    public void getAuthorWithAgeGreaterThan_validAge_success() {
        Author authorA = TestDataUtil.createTestAuthorA(); // age 50
        Author authorB = TestDataUtil.createTestAuthorB(); // age 72
        Author authorC = TestDataUtil.createTestAuthorC(); // age 81
        underTest.saveAll(List.of(authorA, authorB, authorC));

        Iterable<Author> result = underTest.findAuthorWithAgeGreaterThan(75);

        assertThat(result).containsExactly(authorC);
    }
}
