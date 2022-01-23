package com.tusqajavacourse.javacourse.exposure.service;

import com.tusqajavacourse.javacourse.exposure.model.Book;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.*;
import java.util.stream.Collectors;

@Service
@ApplicationScope
public class BooksService {

    private List<Book> books;

    public BooksService() {
        this.books = new ArrayList<>();
    }

    public List<Book> postBook(Book book) {
        this.books.add(book);
        return this.books;
    }

    public List<Book> getBooks() {
        return this.books;
    }

    public Optional<Book> getBookById(String id) {
        return this.books.stream().filter(book -> Objects.equals(book.getUniqueID(), id)).findAny();
    }

    public Optional<Book> getBookByTitle(String title) {
        return this.books.stream().filter(book -> book.getTitle().contains(title)).findAny();
    }

    public List<Book> getBooksSortedByTitle() {
        List<Book> booksToSort = getBooks();
        List<Book> sortedList = booksToSort.stream()
                .sorted(Comparator.comparing(Book::getTitle)).toList();
        return sortedList;
    }

    public Optional<List<Book>> filterBooksByGenre(String genre) {
        return Optional.of(this.books.stream().filter(book -> book.getGenre().contains(genre)).collect(Collectors.toList()));
    }

    public List<Book> getBooksSortedScoreMin() {
        List<Book> booksToSort = getBooks();
        List<Book> sortedByScoreMin = booksToSort.stream()
                .sorted(Comparator.comparing(Book::getScore)).toList();
        return sortedByScoreMin;
    }

    public List<Book> getBooksSortedScoreMax() {
        List<Book> booksToSort = getBooks();
        List<Book> sortedByScoreMax = booksToSort.stream()
                .sorted(Comparator.comparing(Book::getScore).reversed()).toList();
        return sortedByScoreMax;
    }

    public void updateBook(String id, Book newBook) {
        this.books.set(books.indexOf(getBookById(id).orElseThrow()), newBook);
    }

    public void deleteBookById(String id) {
        this.books.removeIf(book -> Objects.equals(book.getUniqueID(), id));
    }
}
