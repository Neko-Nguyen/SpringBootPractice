package com.carameow.realdatabase.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.carameow.realdatabase.TestDataUtil;
import com.carameow.realdatabase.domain.dto.BookDto;
import com.carameow.realdatabase.domain.entities.BookEntity;
import com.carameow.realdatabase.services.BookService;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    private final MockMvc mockMvc;
    private final BookService bookService;
    private final ObjectMapper objectMapper;

    @Autowired
    public BookControllerIntegrationTest(MockMvc mockMvc, BookService bookService) {
        this.mockMvc = mockMvc;
        this.bookService = bookService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void createBook_validInputWithoutAuthor_returnSavedBook() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        String bookDtoJson = objectMapper.writeValueAsString(bookDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookDto.getIsbn())
                        .contentType("application/json")
                        .content(bookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookDto.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookDto.getTitle())
        );
    }

    @Test
    public void listBook_validInput_returnListOfBooks() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookEntityA(null);
        bookService.createUpdateBook(bookEntity.getIsbn(), bookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].isbn").value(bookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].title").value(bookEntity.getTitle())
        );
    }

    @Test
    public void getBook_validInput_returnSavedBook() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookEntityA(null);
        bookService.createUpdateBook(bookEntity.getIsbn(), bookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + bookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookEntity.getTitle())
        );
    }

    @Test
    public void getBook_validInputWithoutExistingBook_notFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/1")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void fullUpdateBook_validInput_returnSavedUpdatedBook() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookEntityB(null);
        bookService.createUpdateBook(bookEntity.getIsbn(), bookEntity);

        BookDto updatedBookDto = TestDataUtil.createTestBookDtoA(null);
        String updatedBookDtoJson = objectMapper.writeValueAsString(updatedBookDto);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/books/" + bookEntity.getIsbn())
                        .contentType("application/json")
                        .content(updatedBookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(updatedBookDto.getTitle())
        );
    }

    @Test
    public void partialUpdateBook_validInputUpdateTitle_returnUpdatedBook() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookEntityB(null);
        bookService.createUpdateBook(bookEntity.getIsbn(), bookEntity);

        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        BookDto bookDtoWithUpdatedTitle = BookDto
                .builder()
                .title(bookDto.getTitle())
                .build();
        String updatedBookDtoJson = objectMapper.writeValueAsString(bookDtoWithUpdatedTitle);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .patch("/books/" + bookEntity.getIsbn())
                        .contentType("application/json")
                        .content(updatedBookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookDtoWithUpdatedTitle.getTitle())
        );
    }

    @Test
    public void partialUpdateBook_validInputWithoutExistingBook_returnUpdatedBook() throws Exception {
        BookDto bookDto = TestDataUtil.createTestBookDtoA(null);
        BookDto bookDtoWithUpdatedTitle = BookDto
                .builder()
                .title(bookDto.getTitle())
                .build();
        String updatedBookDtoJson = objectMapper.writeValueAsString(bookDtoWithUpdatedTitle);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .patch("/books/" + bookDto.getIsbn())
                        .contentType("application/json")
                        .content(updatedBookDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void deleteBook_validInput_noContent() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBookEntityB(null);
        bookService.createUpdateBook(bookEntity.getIsbn(), bookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/" + bookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

}
