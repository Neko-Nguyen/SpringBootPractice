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

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthorEntityRepositoryIntegrationTests {

    private final AuthorRepository underTest;

    @Autowired
    public AuthorEntityRepositoryIntegrationTests(AuthorRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void createAndFindOne_validAuthor_success() {
        AuthorEntity authorA = TestDataUtil.createTestAuthorEntityA();
        underTest.save(authorA);

        Optional<AuthorEntity> result = underTest.findById(authorA.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorA);
    }

    @Test
    public void createAndFind_multipleValidAuthors_success() {
        AuthorEntity authorA = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity authorB = TestDataUtil.createTestAuthorEntityB();
        AuthorEntity authorC = TestDataUtil.createTestAuthorEntityC();
        underTest.saveAll(List.of(authorA, authorB, authorC));

        Iterable<AuthorEntity> results = underTest.findAll();

        assertThat(results)
                .hasSize(3)
                .containsExactly(authorA, authorB, authorC);
    }

    @Test
    public void createAndUpdate_validAuthor_success() {
        AuthorEntity authorA = TestDataUtil.createTestAuthorEntityA();
        underTest.save(authorA);

        authorA.setName("Updated Name");
        underTest.save(authorA);

        Optional<AuthorEntity> result = underTest.findById(authorA.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorA);
    }

    @Test
    public void createAndDelete_validAuthor_success() {
        AuthorEntity authorB = TestDataUtil.createTestAuthorEntityB();
        underTest.save(authorB);

        underTest.deleteById(authorB.getId());
        Optional<AuthorEntity> result = underTest.findById(authorB.getId());

        assertThat(result).isEmpty();
    }

    @Test
    public void getAuthorWithAgeLessThan_validAge_success() {
        AuthorEntity authorA = TestDataUtil.createTestAuthorEntityA(); // age 50
        AuthorEntity authorB = TestDataUtil.createTestAuthorEntityB(); // age 72
        AuthorEntity authorC = TestDataUtil.createTestAuthorEntityC(); // age 81
        underTest.saveAll(List.of(authorA, authorB, authorC));

        Iterable<AuthorEntity> result = underTest.ageLessThan(75);

        assertThat(result).containsExactly(authorA, authorB);
    }

    @Test
    public void getAuthorWithAgeGreaterThan_validAge_success() {
        AuthorEntity authorA = TestDataUtil.createTestAuthorEntityA(); // age 50
        AuthorEntity authorB = TestDataUtil.createTestAuthorEntityB(); // age 72
        AuthorEntity authorC = TestDataUtil.createTestAuthorEntityC(); // age 81
        underTest.saveAll(List.of(authorA, authorB, authorC));

        Iterable<AuthorEntity> result = underTest.findAuthorWithAgeGreaterThan(75);

        assertThat(result).containsExactly(authorC);
    }
}
