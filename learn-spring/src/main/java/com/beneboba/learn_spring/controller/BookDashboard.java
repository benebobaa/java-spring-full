package com.beneboba.learn_spring.controller;

import com.beneboba.learn_spring.model.Book;
import com.beneboba.learn_spring.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookDashboard {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public String findAll(@RequestParam(required = false) String query, Model model) {
        Iterable<Book> books;
        if (query != null && !query.isEmpty()) {
            books = bookRepository.findByTitle(query);
        } else {
            books = bookRepository.findAll();
        }
        model.addAttribute("books", books);
        return "book";
    }

    @GetMapping("/title/{bookTitle}")
    public List<Book> findByTitle(@PathVariable String bookTitle) {
        return bookRepository.findByTitle(bookTitle);
    }

    @PostMapping("/save")
    public String createOrUpdate(@ModelAttribute Book book) {
        if (book.getId() != null) {
            Book existingBook = bookRepository.findById(book.getId()).get();
            existingBook.setAuthor(book.getAuthor());
            existingBook.setTitle(book.getTitle());
            bookRepository.save(existingBook);
        } else {
            Book newBook = new Book();
            newBook.setAuthor(book.getAuthor());
            newBook.setTitle(book.getTitle());
            bookRepository.save(newBook);
        }
        return "redirect:/books";
    }

    @GetMapping({"/form", "/form/{id}"})
    public String form(Model model, @PathVariable(required = false) Long id) {
        Book book = (id != null) ? bookRepository.findById(id).orElse(new Book()) : new Book();
        model.addAttribute("book", book);
        return "form";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable long id) {
        bookRepository.deleteById(id);
        return "redirect:/books";
    }
}