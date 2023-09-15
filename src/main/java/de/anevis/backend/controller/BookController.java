package de.anevis.backend.controller;

import de.anevis.backend.domain.Book;
import de.anevis.backend.domain.SenderBookDTO;
import de.anevis.backend.exception.BookNotFoundException;
import de.anevis.backend.exception.DuplicateBookException;
import de.anevis.backend.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<SenderBookDTO> findAllBooks(
            @RequestParam(value = "cursor", required = false, defaultValue = "") String cursor,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "direction", required = false) String direction,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "first_publish_year", required = false) Integer year
    ) {
        try {
            SenderBookDTO bookDTO = bookService.findAllBooks(cursor, limit, direction, title, year);
            return ResponseEntity.ok(bookDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addBook(@RequestBody Book book) {
        try {
            System.out.println(book);
            bookService.addBook(book);

        } catch (DuplicateBookException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") Long id, @RequestBody Book book) {
        try {
          Book updatedBook = bookService.updateBook(id, book);
            return ResponseEntity.ok(updatedBook);

        } catch (BookNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }catch (DuplicateBookException e) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteBook(
            @PathVariable("id") Long id) {

        try {
            bookService.deleteBook(id);

        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
        }
    }


}

