package com.carameow.realdatabase.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.carameow.realdatabase.domain.entities.AuthorEntity;
import com.carameow.realdatabase.repositories.AuthorRepository;
import com.carameow.realdatabase.services.AuthorService;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorEntity saveAuthor(AuthorEntity author) {
        return authorRepository.save(author);
    }

    @Override
    public List<AuthorEntity> findAll() {
        return StreamSupport
                .stream(authorRepository
                        .findAll()
                        .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AuthorEntity> findAll(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    @Override
    public Optional<AuthorEntity> findOne(Long id) {
        return authorRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return authorRepository.existsById(id);
    }

    @Override
    public AuthorEntity partialUpdate(Long id, AuthorEntity author) {
        author.setId(id);
        return authorRepository
                .findById(id)
                .map(existingAuthor -> {
                    Optional.ofNullable(author.getName()).ifPresent(existingAuthor::setName);
                    Optional.ofNullable(author.getAge()).ifPresent(existingAuthor::setAge);
                    return authorRepository.save(existingAuthor);
                })
                .orElseThrow(() -> new RuntimeException("Author does not exist"));
    }

    @Override
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }

}
