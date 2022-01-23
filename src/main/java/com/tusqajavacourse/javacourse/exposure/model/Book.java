package com.tusqajavacourse.javacourse.exposure.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Document
public class Book {
    private static final AtomicInteger count = new AtomicInteger(0);

    @Id
    private String uniqueID;
    private String author;
    private String title;
    private String description;
    private String genre;
    private double score;

    public Book(String author, String title, String description, String genre, double score) {
        this.uniqueID = Integer.toString(count.incrementAndGet());
        this.author = author;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.score = score;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void updateFields(Book book) {
        this.author = book.author;
        this.title = book.title;
        this.description = book.description;
        this.genre = book.genre;
        this.score = book.score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Double.compare(book.score, score) == 0 && Objects.equals(uniqueID, book.uniqueID) && Objects.equals(author, book.author) && Objects.equals(title, book.title) && Objects.equals(description, book.description) && Objects.equals(genre, book.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueID, author, title, description, genre, score);
    }
}
