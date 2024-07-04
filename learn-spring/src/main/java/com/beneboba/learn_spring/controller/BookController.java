package com.beneboba.learn_spring.controller;

import com.beneboba.learn_spring.exception.BookIdMismatchException;
import com.beneboba.learn_spring.exception.BookNotFoundException;
import com.beneboba.learn_spring.model.Book;
import com.beneboba.learn_spring.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public Iterable<Book> findAll() {
        return bookRepository.findAll();
    }

    @GetMapping("/title/{bookTitle}")
    public List<Book> findByTitle(@PathVariable String bookTitle) {
        return bookRepository.findByTitle(bookTitle);
    }

    @GetMapping("/{id}")
    public Book findOne(@PathVariable long id) {
        return bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Book updateBook(@RequestBody Book book, @PathVariable long id) {
        Book updatedBook = bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);

        if (updatedBook.getId() != id) {
            throw new BookIdMismatchException();
        }
        updatedBook.setAuthor(book.getAuthor());
        updatedBook.setTitle(book.getTitle());
        return bookRepository.save(updatedBook);
    }
}