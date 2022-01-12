package com.tusqajavacourse.javacourse;

import com.tusqajavacourse.javacourse.exposure.model.Book;
import com.tusqajavacourse.javacourse.exposure.service.BooksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BooksServiceTests {

    private static final List<Book> library = Arrays.asList(
            new Book("Bartosz Bartkowski", "Spring By Bartosz", "Description by Bartosz", "Bartoszowe", 9.2),
            new Book("Adam Adamski", "Adams Java", "Description by Adam", "Adamowe", 9.1),
            new Book("Damian Damianski", "Maven By Damian", "Description by Damian", "Damianowe", 9.4),
            new Book("Cyryl Cyrylski", "Cyryls Git", "Description by Cyryl", "Cyrylowe", 9.3)
    );

    private BooksService libraryService;

    @BeforeEach
    void reset() {
        libraryService = new BooksService();
    }

    @Test
    void postAndGetSingleBook() {
        libraryService.postBook(library.get(0));

        List<Book> book = libraryService.getBooks();
        assertAll(
                () -> assertEquals("Spring By Bartosz", book.get(0).getTitle()),
                () -> assertNotEquals("Adams Java", book.get(0).getTitle())
        );
    }

    @Test
    void postAndGetMultipleBooks() {
        library.forEach(libraryService::postBook);
        assertAll(
                () -> assertEquals(4, libraryService.getBooks().size()),
                () -> assertEquals("Bartosz Bartkowski", library.get(0).getAuthor()),
                () -> assertEquals("Cyryl Cyrylski", library.get(3).getAuthor())
        );
    }

    @Test
    void getBookById() {
        library.forEach(libraryService::postBook);

        Optional<Book> bookOne = libraryService.getBookById(1);
        Optional<Book> bookTwo = libraryService.getBookById(4);
        Book newBookOne = bookOne.get();
        Book newBookTwo = bookTwo.get();
        assertEquals("Bartosz Bartkowski", newBookOne.getAuthor());
        assertEquals("Cyryl Cyrylski", newBookTwo.getAuthor());
    }

    @Test
    void getBookByTitle() {
        library.forEach(libraryService::postBook);

        Optional<Book> bookOne = libraryService.getBookByTitle("Adams Java");
        Optional<Book> bookTwo = libraryService.getBookByTitle("Maven By Damian");
        Book newBookOne = bookOne.get();
        Book newBookTwo = bookTwo.get();
        assertEquals("Adam Adamski", newBookOne.getAuthor());
        assertEquals("Damian Damianski", newBookTwo.getAuthor());
    }

    @Test
    void getAbsentBook() {
        library.forEach(libraryService::postBook);

        Optional<Book> book = libraryService.getBookByTitle("Tomasz Wiremock");
        assertFalse(book.isPresent());
    }

    @Test
    void getAllBooksSortedByTitle() {
        library.forEach(libraryService::postBook);
        List<Book> booksToSort = libraryService.getBooksSortedByTitle();
        assertAll(
                () -> assertEquals("Adams Java", booksToSort.get(0).getTitle()),
                () -> assertEquals("Cyryls Git", booksToSort.get(1).getTitle()),
                () -> assertEquals("Maven By Damian", booksToSort.get(2).getTitle()),
                () -> assertEquals("Spring By Bartosz", booksToSort.get(3).getTitle())
        );
    }

    @Test
    void getBooksFilteredByGenre() {
        library.forEach(libraryService::postBook);
        Optional<List<Book>> books = libraryService.filterBooksByGenre("Damianowe");
        List<Book> booksFiltered = books.get();
        assertAll(
                () -> assertEquals(1, booksFiltered.size()),
                () -> assertEquals("Damian Damianski", booksFiltered.get(0).getAuthor()),
                () -> assertEquals("Damianowe", booksFiltered.get(0).getGenre())
        );
    }

    @Test
    void getAllBooksSortedByScoreMin() {
        library.forEach(libraryService::postBook);
        List<Book> booksToSort = libraryService.getBooksSortedScoreMin();
        assertAll(
                () -> assertEquals("Adams Java", booksToSort.get(0).getTitle()),
                () -> assertEquals(9.1, booksToSort.get(0).getScore()),
                () -> assertEquals("Spring By Bartosz", booksToSort.get(1).getTitle()),
                () -> assertEquals(9.2, booksToSort.get(1).getScore()),
                () -> assertEquals("Cyryls Git", booksToSort.get(2).getTitle()),
                () -> assertEquals(9.3, booksToSort.get(2).getScore()),
                () -> assertEquals("Maven By Damian", booksToSort.get(3).getTitle()),
                () -> assertEquals(9.4, booksToSort.get(3).getScore())
        );
    }

    @Test
    void getAllBooksSortedByScoreMax() {
        library.forEach(libraryService::postBook);
        List<Book> booksToSort = libraryService.getBooksSortedScoreMax();
        assertAll(
                () -> assertEquals("Maven By Damian", booksToSort.get(0).getTitle()),
                () -> assertEquals(9.4, booksToSort.get(0).getScore()),
                () -> assertEquals("Cyryls Git", booksToSort.get(1).getTitle()),
                () -> assertEquals(9.3, booksToSort.get(1).getScore()),
                () -> assertEquals("Spring By Bartosz", booksToSort.get(2).getTitle()),
                () -> assertEquals(9.2, booksToSort.get(2).getScore()),
                () -> assertEquals("Adams Java", booksToSort.get(3).getTitle()),
                () -> assertEquals(9.1, booksToSort.get(3).getScore())
        );
    }

    @Test
    void updateBook() {
        library.forEach(libraryService::postBook);
        libraryService.updateBook(1,
                new Book("Alfred Alfredowski", "Spring By Bartosz", "Description by Bartosz", "Bartoszowe", 9.2));
        assertAll(
                () -> assertEquals("Alfred Alfredowski", libraryService.getBookById(5).get().getAuthor()),
                () -> assertEquals("Spring By Bartosz", libraryService.getBookById(5).get().getTitle())
        );
    }

    @Test
    void deleteBook() {
        library.forEach(libraryService::postBook);
        libraryService.deleteBookById(1);

        List<Book> books = libraryService.getBooks();
        assertAll(
                () -> assertEquals(3, books.size()),
                () -> assertFalse(library.get(0).getAuthor().isEmpty())
        );
    }
}
