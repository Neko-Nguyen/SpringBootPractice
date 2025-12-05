package com.carameow.realdatabase.dao;

import java.util.List;
import java.util.Optional;

import com.carameow.realdatabase.domain.Book;

public interface BookDAO {

    void create(Book book);

    Optional<Book> findOne(String s);

    List<Book> find();

    void update(String isbn, Book book);

    void delete(String s);
}
