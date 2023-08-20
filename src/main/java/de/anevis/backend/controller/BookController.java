package de.anevis.backend.controller;

import de.anevis.backend.domain.Book;
import de.anevis.backend.exception.BookNotFoundException;
import de.anevis.backend.exception.DuplicateBookException;
import de.anevis.backend.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> findAllBooks() {
        try {
            List<Book> books = bookService.findAll();
            return ResponseEntity.ok(books);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        try {
            Book addedBook = bookService.addBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedBook);

        } catch (DuplicateBookException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable("id") Long id, @RequestBody Book updatedBook) {
        try {
            Book updated = bookService.updateBook(id, updatedBook);
                return ResponseEntity.ok(updated);

        } catch (BookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();

        } catch (BookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}

