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
import com.carameow.realdatabase.domain.dto.AuthorDto;
import com.carameow.realdatabase.domain.entities.AuthorEntity;
import com.carameow.realdatabase.services.AuthorService;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTest {

    private final MockMvc mockMvc;
    private final AuthorService authorService;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthorControllerIntegrationTest(MockMvc mockMvc, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.authorService = authorService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void createAuthor_validInput_returnSavedAuthor() throws Exception {
        AuthorDto authorDtoA = TestDataUtil.createTestAuthorDtoA();
        String authorDtoJson = objectMapper.writeValueAsString(authorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/authors")
                        .contentType("application/json")
                        .content(authorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorDtoA.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorDtoA.getAge())
        );
    }

    @Test
    public void listAuthors_validInput_returnListOfAuthors() throws Exception {
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorEntityA();
        authorService.saveAuthor(authorEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].name").value(authorEntityA.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.content[0].age").value(authorEntityA.getAge())
        );
    }

    @Test
    public void getAuthor_validInput_returnSavedAuthor() throws Exception {
        AuthorEntity authorEntityA = TestDataUtil.createTestAuthorEntityA();
        authorService.saveAuthor(authorEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/" + authorEntityA.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(authorEntityA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorEntityA.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorEntityA.getAge())
        );
    }

    @Test
    public void getAuthor_validInputWithoutExistingAuthor_notFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void fullUpdateAuthor_validInput_returnSavedUpdatedAuthor() throws Exception {
        AuthorEntity authorEntityB = TestDataUtil.createTestAuthorEntityB();
        authorService.saveAuthor(authorEntityB);

        AuthorDto updatedAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
        String updatedAuthorDtoJson = objectMapper.writeValueAsString(updatedAuthorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/authors/" + authorEntityB.getId())
                        .contentType("application/json")
                        .content(updatedAuthorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(authorEntityB.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(updatedAuthorDtoA.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(updatedAuthorDtoA.getAge())
        );
    }

    @Test
    public void fullUpdateAuthor_validInputWithoutExistingAuthor_notFound() throws Exception {
        AuthorDto updatedAuthorDtoA = TestDataUtil.createTestAuthorDtoA();
        String updatedAuthorDtoJson = objectMapper.writeValueAsString(updatedAuthorDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/authors/1")
                        .contentType("application/json")
                        .content(updatedAuthorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void partialUpdateAuthor_validInputUpdateName_returnUpdatedAuthor() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityB();
        authorService.saveAuthor(authorEntity);

        AuthorDto authorDto = TestDataUtil.createTestAuthorDtoA();
        AuthorDto authorDtoWithUpdatedName = AuthorDto
                .builder()
                .name(authorDto.getName())
                .build();
        String updatedAuthorDtoJson = objectMapper.writeValueAsString(authorDtoWithUpdatedName);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .patch("/authors/" + authorEntity.getId())
                        .contentType("application/json")
                        .content(updatedAuthorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(authorEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorDtoWithUpdatedName.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorEntity.getAge())
        );
    }

    @Test
    public void partialUpdateAuthor_validInputUpdateAge_returnUpdatedAuthor() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityB();
        authorService.saveAuthor(authorEntity);

        AuthorDto authorDto = TestDataUtil.createTestAuthorDtoA();
        AuthorDto authorDtoWithUpdatedAge = AuthorDto
                .builder()
                .age(authorDto.getAge())
                .build();
        String updatedAuthorDtoJson = objectMapper.writeValueAsString(authorDtoWithUpdatedAge);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .patch("/authors/" + authorEntity.getId())
                        .contentType("application/json")
                        .content(updatedAuthorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(authorEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorEntity.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorDtoWithUpdatedAge.getAge())
        );
    }

    @Test
    public void partialUpdateAuthor_validInputWithoutExistingAuthor_notFound() throws Exception {
        AuthorDto authorDto = TestDataUtil.createTestAuthorDtoA();
        AuthorDto authorDtoWithUpdatedAge = AuthorDto
                .builder()
                .age(authorDto.getAge())
                .build();
        String updatedAuthorDtoJson = objectMapper.writeValueAsString(authorDtoWithUpdatedAge);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .patch("/authors/1")
                        .contentType("application/json")
                        .content(updatedAuthorDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void deleteAuthor_validInput_noContent() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityB();
        authorService.saveAuthor(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/1")
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

}
