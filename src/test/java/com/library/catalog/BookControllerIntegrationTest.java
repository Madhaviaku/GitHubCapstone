package com.library.catalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.catalog.dto.BookCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createBookShouldPersistAndReturnCreatedBook() throws Exception {
        BookCreateRequest request = new BookCreateRequest(
                "9783161484100",
                "Effective Java",
                "Joshua Bloch",
                "Addison-Wesley",
                LocalDate.of(2018, 1, 1),
                "Programming",
                5
        );

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.message").value("Book created successfully"))
                .andExpect(jsonPath("$.data.isbn").value("9783161484100"));

        mockMvc.perform(get("/api/books/9783161484100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Effective Java"));
    }

    @Test
    void getBookShouldReturnNotFoundForMissingIsbn() throws Exception {
        mockMvc.perform(get("/api/books/0000000000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Book with ISBN 0000000000 not found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void createBookShouldFailWhenRequiredFieldsMissing() throws Exception {
        String payload = "{\"isbn\":\"\",\"title\":\"\",\"author\":\"\",\"genre\":\"\",\"quantityAvailable\":-1}";

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errors.title").exists());
    }

    @Test
    void searchShouldMatchPartialTitleWithoutCaseSensitivity() throws Exception {
        String payload = """
                {"isbn":"8888888888","title":"The Java Catalog","author":"A Writer","genre":"Technology"}
                """;
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/books/search")
                        .param("title", "jAvA")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].title").value("The Java Catalog"));
    }

    @Test
    void searchShouldMatchAcrossAuthorGenreAndIsbnWithoutCaseSensitivity() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"isbn\":\"9780132350884\",\"title\":\"Clean Code\",\"author\":\"Robert C. Martin\",\"genre\":\"Software Craft\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/books/search")
                        .param("title", "martin")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].author").value("Robert C. Martin"));

        mockMvc.perform(get("/api/books/search")
                        .param("title", "craft")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].genre").value("Software Craft"));

        mockMvc.perform(get("/api/books/search")
                        .param("title", "013235")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].isbn").value("9780132350884"));
    }

    @Test
    void updateRequestCannotChangeIsbn() throws Exception {
        String createPayload = """
                {"isbn":"9999999999","title":"Original Title","author":"A Writer","genre":"Fiction"}
                """;
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/api/books/9999999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"isbn\":\"1234567890\",\"title\":\"Updated Title\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isbn").value("9999999999"))
                .andExpect(jsonPath("$.data.title").value("Updated Title"));
    }
}
