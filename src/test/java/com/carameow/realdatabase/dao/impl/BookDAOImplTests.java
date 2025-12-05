package com.carameow.realdatabase.dao.impl;

import static org.mockito.ArgumentMatchers.any;
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
import com.carameow.realdatabase.domain.Book;

@ExtendWith(MockitoExtension.class)
public class BookDAOImplTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private BookDAOImpl underTest;

    @Test
    public void create_validInput_generateCorrectSql() {
        Book book = TestDataUtil.createTestBookA();

        underTest.create(book);
        verify(jdbcTemplate).update(
                eq("INSERT INTO book (isbn, title, author_id) VALUES (?, ?, ?)"),
                eq("978-3-16-148410-0"), eq("Harry Potter"), eq(1L)
        );
    }

    @Test
    public void findOne_validInput_generateCorrectSql() {
        underTest.findOne("978-3-16-148410-0");
        verify(jdbcTemplate).query(
                eq("SELECT isbn, title, author_id FROM book WHERE isbn = ? LIMIT 1"),
                ArgumentMatchers.<BookDAOImpl.BookRowMapper>any(),
                eq("978-3-16-148410-0")
        );
    }

    @Test
    public void find_validInput_generateCorrectSql() {
        underTest.find();
        verify(jdbcTemplate).query(
                eq("SELECT isbn, title, author_id FROM book"),
                ArgumentMatchers.<BookDAOImpl.BookRowMapper>any()
        );
    }

    @Test
    public void update_validInput_generateCorrectSql() {
        Book book = TestDataUtil.createTestBookA();
        underTest.update("978-3-16-158410-0", book);
        verify(jdbcTemplate).update(
                eq("UPDATE book SET title = ?, author_id = ? WHERE isbn = ?"),
                eq(book.getTitle()), eq(book.getAuthorId()), eq("978-3-16-158410-0")
        );
    }

    @Test
    public void delete_validInput_generateCorrectSql() {
        underTest.delete("978-3-16-148410-0");
        verify(jdbcTemplate).update(
                eq("DELETE FROM book WHERE isbn = ?"),
                eq("978-3-16-148410-0")
        );
    }
}
