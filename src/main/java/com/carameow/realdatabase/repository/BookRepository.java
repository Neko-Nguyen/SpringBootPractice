package com.carameow.realdatabase.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.carameow.realdatabase.domain.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, String> {
}
