package com.tusqajavacourse.javacourse.exposure.service;

import com.tusqajavacourse.javacourse.exposure.model.Book;
import com.tusqajavacourse.javacourse.exposure.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@ApplicationScope
public class BooksServiceDB {

    private BookRepository bookRepository;

    @Autowired
    public BooksServiceDB(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book postBookDB(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getBooksDB() {
        return this.bookRepository.findAll();
    }

    public Optional<Book> getBookByIdDB(String id) {
        return bookRepository.findById(id);
    }

//    public Optional<Book> getBookByTitle(String title) {
//        return this.books.stream().filter(book -> book.getTitle().contains(title)).findAny();
//    }
//
//    public List<Book> getBooksSortedByTitle() {
//        List<Book> booksToSort = getBooks();
//        List<Book> sortedList = booksToSort.stream()
//                .sorted(Comparator.comparing(Book::getTitle)).toList();
//        return sortedList;
//    }
//
//    public Optional<List<Book>> filterBooksByGenre(String genre) {
//        return Optional.of(this.books.stream().filter(book -> book.getGenre().contains(genre)).collect(Collectors.toList()));
//    }
//
//    public List<Book> getBooksSortedScoreMin() {
//        List<Book> booksToSort = getBooks();
//        List<Book> sortedByScoreMin = booksToSort.stream()
//                .sorted(Comparator.comparing(Book::getScore)).toList();
//        return sortedByScoreMin;
//    }
//
//    public List<Book> getBooksSortedScoreMax() {
//        List<Book> booksToSort = getBooks();
//        List<Book> sortedByScoreMax = booksToSort.stream()
//                .sorted(Comparator.comparing(Book::getScore).reversed()).toList();
//        return sortedByScoreMax;
//    }
//
    public void updateBookDB(String id, Book newBook) {
        Optional<Book> bookOptional = getBookByIdDB(id);
        bookOptional.ifPresent(book -> {
            book.updateFields(newBook);
            bookRepository.save(book);
        });
    }

    public void deleteBookByIdDB(String id) {
        Optional<Book> book = getBookByIdDB(id);
        book.ifPresent(bookToDelete -> bookRepository.delete(bookToDelete));
    }
}
