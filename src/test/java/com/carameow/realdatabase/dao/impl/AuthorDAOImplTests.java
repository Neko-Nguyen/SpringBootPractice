package com.carameow.realdatabase.dao.impl;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import com.carameow.realdatabase.TestDataUtil;
import com.carameow.realdatabase.domain.Author;

@ExtendWith(MockitoExtension.class)
public class AuthorDAOImplTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private AuthorDAOImpl underTest;

    @Test
    public void create_validInput_generateCorrectSql() {
        Author author = TestDataUtil.createTestAuthorA();

        underTest.create(author);
        verify(jdbcTemplate).update(
                eq("INSERT INTO author (id, name, age) VALUES (?, ?, ?)"),
                eq(1L), eq("JK Rolling"), eq(50)
        );
    }

    @Test
    public void findOne_validInput_generateCorrectSql() {
        underTest.findOne(1L);
        verify(jdbcTemplate).query(
                eq("SELECT id, name, age FROM author WHERE id = ? LIMIT 1"),
                ArgumentMatchers.<AuthorDAOImpl.AuthorRowMapper>any(),
                eq(1L)
        );
    }

    @Test
    public void findMany_validInput_generateCorrectSql() {
        underTest.find();
        verify(jdbcTemplate).query(
                eq("SELECT id, name, age FROM author"),
                ArgumentMatchers.<AuthorDAOImpl.AuthorRowMapper>any()
        );
    }

    @Test
    public void update_validInput_generateCorrectSql() {
        Author author = TestDataUtil.createTestAuthorA();
        underTest.update(3L, author);
        verify(jdbcTemplate).update(
                eq("UPDATE author SET name = ?, age = ? WHERE id = ?"),
                eq(author.getName()), eq(author.getAge()), eq(3L)
        );
    }

    @Test
    public void delete_validInput_generateCorrectSql() {
        underTest.delete(2L);
        verify(jdbcTemplate).update(
                eq("DELETE FROM author WHERE id = ?"),
                eq(2L)
        );
    }
}
