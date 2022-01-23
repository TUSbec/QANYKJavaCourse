package com.tusqajavacourse.javacourse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tusqajavacourse.javacourse.exposure.controller.BookController;
import com.tusqajavacourse.javacourse.exposure.model.Book;
import com.tusqajavacourse.javacourse.exposure.service.BooksService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
public class BookControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BooksService booksService;

    private static final List<Book> library = Arrays.asList(
            new Book("Bartosz Bartkowski", "Spring By Bartosz", "Description by Bartosz", "Bartoszowe", 9.2),
            new Book("Adam Adamski", "Adams Java", "Description by Adam", "Adamowe", 9.1),
            new Book("Damian Damianski", "Maven By Damian", "Description by Damian", "Damianowe", 9.4),
            new Book("Cyryl Cyrylski", "Cyryls Git", "Description by Cyryl", "Cyrylowe", 9.3)
    );

    @Test
    void postBooks() throws Exception {
        Book addedBookOne = library.get(0);
        Book addedBookTwo = library.get(3);
        Mockito.when(booksService.postBook(addedBookOne)).thenReturn(Collections.singletonList(addedBookOne));
        Mockito.when(booksService.postBook(addedBookTwo)).thenReturn(Collections.singletonList(addedBookTwo));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/NYKLibrary/POSTbooks")
                        .content(mapToJson(addedBookOne))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.title", is("Spring By Bartosz")));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/NYKLibrary/POSTbooks")
                        .content(mapToJson(addedBookTwo))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.title", is("Cyryls Git")));
    }

    @Test
    void getAllBooks() throws Exception {
        Mockito.when(booksService.getBooks()).thenReturn(library);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/NYKLibrary/GETbooks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].title", is("Spring By Bartosz")))
                .andExpect(jsonPath("$[3].title", is("Cyryls Git")));
    }

    @Test
    void getBookById() throws Exception {
        Mockito.when(booksService.getBookById("1")).thenReturn(Optional.ofNullable(library.get(0)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/NYKLibrary/GETbooksById/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.title", is("Spring By Bartosz")));
    }

    @Test
    void getBookByTitle() throws Exception {
        Mockito.when(booksService.getBookByTitle("Spring By Bartosz")).thenReturn(Optional.ofNullable(library.get(0)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/NYKLibrary/GETbooksByTitle/Spring By Bartosz")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.author", is("Bartosz Bartkowski")));
    }

    @Test
    void getAllBooksSortedByTitle() throws Exception {
        Mockito.when(booksService.getBooksSortedByTitle()).thenReturn(library.stream()
                .sorted(Comparator.comparing(Book::getTitle)).toList());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/NYKLibrary/GETbooks/sortbytitle")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[0].title", is("Adams Java")))
                .andExpect(jsonPath("$[1].title", is("Cyryls Git")))
                .andExpect(jsonPath("$[2].title", is("Maven By Damian")))
                .andExpect(jsonPath("$[3].title", is("Spring By Bartosz")));
    }

    @Test
    void getBooksFilteredByGenre() throws Exception {
        Mockito.when(booksService.filterBooksByGenre("Cyrylowe"))
                .thenReturn(Optional.of(Collections.singletonList(library.get(3))));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/NYKLibrary/GETbooks/filter/genre?genre=Cyrylowe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].author", is("Cyryl Cyrylski")))
                .andExpect(jsonPath("$[0].genre", is("Cyrylowe")));
    }

    @Test
    void getAllBooksSortedByScoreMin() throws Exception {
        Mockito.when(booksService.getBooksSortedScoreMin()).thenReturn(library.stream()
                .sorted(Comparator.comparing(Book::getScore)).toList());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/NYKLibrary/GETbooks/scoreMin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[0].title", is("Adams Java")))
                .andExpect(jsonPath("$[0].score", is(9.1)))
                .andExpect(jsonPath("$[1].title", is("Spring By Bartosz")))
                .andExpect(jsonPath("$[1].score", is(9.2)))
                .andExpect(jsonPath("$[2].title", is("Cyryls Git")))
                .andExpect(jsonPath("$[2].score", is(9.3)))
                .andExpect(jsonPath("$[3].title", is("Maven By Damian")))
                .andExpect(jsonPath("$[3].score", is(9.4)));

    }

    @Test
    void getAllBooksSortedByScoreMax() throws Exception {
        Mockito.when(booksService.getBooksSortedScoreMax()).thenReturn(library.stream()
                .sorted(Comparator.comparing(Book::getScore).reversed()).toList());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/NYKLibrary/GETbooks/scoreMax")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[0].title", is("Maven By Damian")))
                .andExpect(jsonPath("$[0].score", is(9.4)))
                .andExpect(jsonPath("$[1].title", is("Cyryls Git")))
                .andExpect(jsonPath("$[1].score", is(9.3)))
                .andExpect(jsonPath("$[2].title", is("Spring By Bartosz")))
                .andExpect(jsonPath("$[2].score", is(9.2)))
                .andExpect(jsonPath("$[3].title", is("Adams Java")))
                .andExpect(jsonPath("$[3].score", is(9.1)));
    }

    @Test
    void updateBook() throws Exception {
        Book newBookOne = new Book("Alfred Alfredowski", "Spring By Bartosz", "Description by Bartosz", "Bartoszowe", 9.2);
        Mockito.when(booksService.getBookById("1")).thenReturn(Optional.ofNullable(library.get(0)));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/NYKLibrary/updateBooks/1")
                        .content(mapToJson(newBookOne))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.author", is("Alfred Alfredowski")))
                .andExpect(jsonPath("$.title", is("Spring By Bartosz")));
    }

    @Test
    void deleteBook() throws Exception {
        Mockito.when(booksService.getBookById("1")).thenReturn(Optional.ofNullable(library.get(0)));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/NYKLibrary/deleteBooks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$").doesNotExist());
    }

    static String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }
}
