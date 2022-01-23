package com.tusqajavacourse.javacourse.exposure.controller;

import com.tusqajavacourse.javacourse.exposure.model.Book;
import com.tusqajavacourse.javacourse.exposure.service.BooksServiceDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Controller
//@ResponseBody
@RestController
@RequestMapping("/NYKLibraryDB")
public class BookControllerDB {

    private BooksServiceDB booksServiceDB;

    @Autowired
    public BookControllerDB(BooksServiceDB booksServiceDB) {
        this.booksServiceDB = booksServiceDB;
    }

    @PostMapping("/POSTbooksDB")
    public ResponseEntity<Book> postBook(@RequestBody Book book) {
        try {
            booksServiceDB.postBookDB(book);
            return new ResponseEntity<>(book, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/GETbooksDB")
    public List<Book> getBookDB() {
        return booksServiceDB.getBooksDB();
    }

    @GetMapping("/GETbooksByIdDB/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") String id) {
        return booksServiceDB.getBookByIdDB(id).isPresent() ?
                new ResponseEntity<>(booksServiceDB.getBookByIdDB(id).get(), HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

//    @GetMapping("/GETbooksByTitle/{title}")
//    public ResponseEntity<Book> getBook(@PathVariable("title") String title) {
//        return booksService.getBookByTitle(title).isPresent() ?
//                new ResponseEntity<>(booksService.getBookByTitle(title).get(), HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//    }
//
//    @GetMapping("/GETbooks/sortbytitle")
//    public List<Book> getBookSortedByTitle() {
//        return booksService.getBooksSortedByTitle();
//    }
//
//    @GetMapping("/GETbooks/filter/genre")
//    public ResponseEntity<List<Book>> getBooksFilerByGenre(@RequestParam(value = "genre") String genre) {
//        return booksService.filterBooksByGenre(genre).isPresent() ?
//                new ResponseEntity<>(booksService.filterBooksByGenre(genre).get(), HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//    }
//
//    @GetMapping("/GETbooks/scoreMin")
//    public List<Book> getBookSortedScoreMin() {
//        return booksService.getBooksSortedScoreMin();
//    }
//
//    @GetMapping("/GETbooks/scoreMax")
//    public List<Book> getBookSortedScoreMax() {
//        return booksService.getBooksSortedScoreMax();
//    }
//
    @PutMapping("/updateBooksDB/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") String id, @RequestBody Book book) {
        try {
            booksServiceDB.updateBookDB(id, book);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteBooksDB/{id}")
    public void deleteBook(@PathVariable("id") String id) {
        booksServiceDB.deleteBookByIdDB(id);
    }
}
