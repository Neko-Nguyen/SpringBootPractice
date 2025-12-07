package com.carameow.realdatabase.controllers;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.carameow.realdatabase.domain.dto.BookDto;
import com.carameow.realdatabase.domain.entities.BookEntity;
import com.carameow.realdatabase.mappers.Mapper;
import com.carameow.realdatabase.services.BookService;

@RestController
public class BookController {

    private final BookService bookService;
    private final Mapper<BookEntity, BookDto> bookMapper;

    public BookController(BookService bookService, Mapper<BookEntity, BookDto> bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> createUpdateBook(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto) {
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        // Dto to Entity
        boolean bookExists = bookService.isExists(isbn);
        BookEntity savedBookEntity = bookService.createUpdateBook(isbn, bookEntity);
        // Entity to Dto
        BookDto savedBookDto = bookMapper.mapTo(savedBookEntity);

        if (!bookExists) {
            return new ResponseEntity<>(savedBookDto, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(savedBookDto, HttpStatus.OK);
    }

    @GetMapping(path = "/books")
    public Page<BookDto> listBook(Pageable pageable) {
        Page<BookEntity> bookEntities = bookService.findAll(pageable);
        return bookEntities.map(bookMapper::mapTo);

        // List all impl direction
//        List<BookDto> bookDtos = bookEntities.stream()
//                .map(bookMapper::mapTo)
//                .collect(Collectors.toList());
//        return bookDtos;
    }

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> getBook(@PathVariable("isbn") String isbn) {
        Optional<BookEntity> foundBookEntity = bookService.findOne(isbn);
        return foundBookEntity
                .map(bookEntity -> {
                    BookDto bookDto = bookMapper.mapTo(bookEntity); // Entity to Dto
                    return new ResponseEntity<>(bookDto, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDto> partialUpdateBook(@PathVariable("isbn") String isbn, @RequestBody BookDto bookDto) {
        if (!bookService.isExists(isbn)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        // Entity to Dto
        BookEntity updatedBookEntity = bookService.partialUpdate(isbn, bookEntity);
        // Dto to Entity
        BookDto updatedBookDto = bookMapper.mapTo(bookEntity);
        return new ResponseEntity<>(updatedBookDto, HttpStatus.OK);
    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity deleteBook(@PathVariable("isbn") String isbn){
        bookService.delete(isbn);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
