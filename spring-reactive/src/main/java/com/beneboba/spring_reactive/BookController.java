package com.beneboba.spring_reactive;

import com.beneboba.spring_reactive.Book;
import com.beneboba.spring_reactive.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping("/books")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Book> getAllBooks(@RequestParam(required = false) String title) {
        if (title == null)
            return bookService.findAll();
        else
            return bookService.findByTitleContaining(title);
    }

    @GetMapping("/books/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Book> getBookById(@PathVariable("id") int id) {
        return bookService.findById(id);
    }

    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Book> createBook(@RequestBody Book book) {
        return bookService.save(new Book(book.getTitle(), book.getDescription(), book.isPublished()));
    }

    @PutMapping("/books/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Book> updateBook(@PathVariable("id") int id, @RequestBody Book book) {
        return bookService.update(id, book);
    }

    @DeleteMapping("/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBook(@PathVariable("id") int id) {
        return bookService.deleteById(id);
    }

    @DeleteMapping("/books")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAllBooks() {
        return bookService.deleteAll();
    }

    @GetMapping("/books/published")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Book> findByPublished() {
        return bookService.findByPublished(true);
    }
}