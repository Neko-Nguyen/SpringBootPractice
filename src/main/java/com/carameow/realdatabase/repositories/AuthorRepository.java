package com.carameow.realdatabase.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.carameow.realdatabase.domain.entities.AuthorEntity;

@Repository
public interface AuthorRepository extends
        CrudRepository<AuthorEntity, Long>,
        PagingAndSortingRepository<AuthorEntity, Long> {
    Iterable<AuthorEntity> ageLessThan(int age);

    @Query("SELECT a FROM AuthorEntity a WHERE a.age > ?1")
    Iterable<AuthorEntity> findAuthorWithAgeGreaterThan(int age);
}
