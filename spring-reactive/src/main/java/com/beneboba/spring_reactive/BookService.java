package com.beneboba.spring_reactive;

import com.beneboba.spring_reactive.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    public Flux<Book> findAll() {
        return bookRepository.findAll();
    }

    public Flux<Book> findByTitleContaining(String title) {
        return bookRepository.findByTitleContaining(title);
    }

    public Mono<Book> findById(int id) {
        return bookRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(
                                new BookNotFoundException(String.format("Book not found. ID: %s", id))
                        )
                );
    }

    public Mono<Book> save(Book Book) {
        return bookRepository.save(Book);
    }

    public Mono<Book> update(int id, Book book) {
        return bookRepository.findById(id)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(optionalBook -> {
                    if (optionalBook.isPresent()) {
                        book.setId(id);
                        return bookRepository.save(book);
                    }

                    return Mono.empty();
                });
    }

    public Mono<Void> deleteById(int id) {
        return bookRepository.deleteById(id);
    }

    public Mono<Void> deleteAll() {
        return bookRepository.deleteAll();
    }

    public Flux<Book> findByPublished(boolean isPublished) {
        return bookRepository.findByPublished(isPublished);
    }
}