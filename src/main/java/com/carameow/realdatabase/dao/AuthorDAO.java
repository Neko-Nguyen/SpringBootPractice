package com.carameow.realdatabase.dao;

import java.util.List;
import java.util.Optional;

import com.carameow.realdatabase.domain.Author;

public interface AuthorDAO {

    void create(Author author);

    Optional<Author> findOne(long id);

    List<Author> find();

    void update(long id, Author author);

    void delete(long id);
}
