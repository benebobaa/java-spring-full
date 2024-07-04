package com.beneboba.spring_reactive;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.test.web.reactive.server.WebTestClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = BookController.class)
@AutoConfigureWebTestClient
@Slf4j
public class BookControllerTest {
    private final WebTestClient webTestClient;

    @MockBean
    private final BookService bookService;

    @MockBean
    private final BookRepository bookRepository;

    @MockBean
    ConnectionFactoryInitializer initializer;

    private final String url = "/api/books";

    @Autowired
    public BookControllerTest(WebTestClient webTestClient, BookService bookService, BookRepository bookRepository) {
        this.webTestClient = webTestClient;
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }

    @Test
    void createBookTest() {
        var newBook = Book.builder().title("New Book Title").description("New Book Description").build();
        var savedBook = Book.builder().id(1).title(newBook.getTitle()).description(newBook.getDescription()).build();

        when(bookService.save(newBook)).thenReturn(Mono.just(savedBook));

        webTestClient
                .post()
                .uri(url)
                .bodyValue(newBook)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Book.class)
                .consumeWith(result -> {
                    var createdBook = result.getResponseBody();

                    assertNotNull(createdBook);
                    assertEquals(savedBook.getId(), createdBook.getId());
                    assertEquals(savedBook.getTitle(), createdBook.getTitle());
                    assertEquals(savedBook.getDescription(), createdBook.getDescription());
                });
    }

    @Test
    void findBookByIdOk() {
        var book = Book.builder().id(1).title("test title 1").description("test description 1").build();
        log.info("book: {}", book);

        when(bookService.findById(book.getId())).thenReturn(Mono.just(book));

        String findBookByIdUrl = String.format("%s/%s", url, book.getId());
        log.info("url: {}", findBookByIdUrl);
        webTestClient
                .get()
                .uri(findBookByIdUrl)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Book.class)
                .consumeWith(result -> {
                    var existBook = result.getResponseBody();

                    assert existBook != null;
                    assertEquals(book.getId(), existBook.getId());
                    assertEquals(book.getTitle(), existBook.getTitle());

                });
    }

    @Test
    void findBookByIdErrorNotFound() {
        var book = Book.builder().id(3).title("test title 3")
                .description("test description 3")
                .build();

        when(bookService.findById(book.getId())).thenReturn(Mono.error(new BookNotFoundException(String.format("Book not found. ID: %s", book.getId()))));

        String findTodoByIdUrl = String.format("%s/%s", url, book.getId());
        webTestClient
                .get()
                .uri(findTodoByIdUrl)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ErrorResponse.class)
                .consumeWith(result -> {
                    var errorResponse = result.getResponseBody();

                    assertNotNull(errorResponse);
                    assertTrue(errorResponse.getStatusCode() != HttpStatus.OK.value());
                    assertEquals("Book not found. ID: 3", errorResponse.getMessage());
                });
    }

    @Test
    void getAllBooksTest() {
        var book1 = Book.builder().id(1).title("Book 1").description("Description 1").build();
        var book2 = Book.builder().id(2).title("Book 2").description("Description 2").build();

        when(bookService.findAll()).thenReturn(Flux.just(book1, book2));

        webTestClient
                .get()
                .uri(url)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Book.class)
                .consumeWith(result -> {
                    var books = result.getResponseBody();
                    assertNotNull(books);
                    assertEquals(2, books.size());
                    assertEquals(book1.getId(), books.get(0).getId());
                    assertEquals(book2.getId(), books.get(1).getId());
                });
    }

    @Test
    void getAllBooksWithTitleTest() {
        var book1 = Book.builder().id(1).title("Book 1").description("Description 1").build();

        when(bookService.findByTitleContaining("Book 1")).thenReturn(Flux.just(book1));

        webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(url).queryParam("title", "Book 1").build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Book.class)
                .consumeWith(result -> {
                    var books = result.getResponseBody();
                    assertNotNull(books);
                    assertEquals(1, books.size());
                    assertEquals(book1.getId(), books.get(0).getId());
                });
    }

    @Test
    void updateBookTest() {
        var bookToUpdate = Book.builder().title("Updated Book Title").description("Updated Book Description").build();
        var updatedBook = Book.builder().id(1).title(bookToUpdate.getTitle()).description(bookToUpdate.getDescription()).build();

        when(bookService.update(1, bookToUpdate)).thenReturn(Mono.just(updatedBook));

        String updateBookUrl = String.format("%s/%s", url, 1);
        webTestClient
                .put()
                .uri(updateBookUrl)
                .bodyValue(bookToUpdate)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Book.class)
                .consumeWith(result -> {
                    var updatedBookResponse = result.getResponseBody();
                    assertNotNull(updatedBookResponse);
                    assertEquals(updatedBook.getId(), updatedBookResponse.getId());
                    assertEquals(updatedBook.getTitle(), updatedBookResponse.getTitle());
                    assertEquals(updatedBook.getDescription(), updatedBookResponse.getDescription());
                });
    }

    @Test
    void deleteBookByIdTest() {
        when(bookService.deleteById(1)).thenReturn(Mono.empty());

        String deleteBookByIdUrl = String.format("%s/%s", url, 1);
        webTestClient
                .delete()
                .uri(deleteBookByIdUrl)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void deleteAllBooksTest() {
        when(bookService.deleteAll()).thenReturn(Mono.empty());

        webTestClient
                .delete()
                .uri(url)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void findByPublishedTest() {
        var book1 = Book.builder().id(1).title("Book 1").description("Description 1").published(true).build();
        var book2 = Book.builder().id(2).title("Book 2").description("Description 2").published(true).build();

        when(bookService.findByPublished(true)).thenReturn(Flux.just(book1, book2));

        String findByPublishedUrl = String.format("%s/published", url);
        webTestClient
                .get()
                .uri(findByPublishedUrl)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Book.class)
                .consumeWith(result -> {
                    var books = result.getResponseBody();
                    assertNotNull(books);
                    assertEquals(2, books.size());
                    assertTrue(books.stream().allMatch(Book::isPublished));
                });
    }
}
