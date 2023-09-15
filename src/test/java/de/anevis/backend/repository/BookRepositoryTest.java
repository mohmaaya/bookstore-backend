package de.anevis.backend.repository;

import static org.junit.jupiter.api.Assertions.*;

import de.anevis.backend.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testSelectBookExists() {
        boolean result = bookRepository.selectBookExists("The Lion, the Witch and the Wardrobe", "The Da Vinci Code");
        assertFalse(result);
    }

    @Test
    public void testFindDuplicateBook() {
        Optional<Book> result = bookRepository.findBook("Le Petit Prince", "Oscar Wilde");
        assertFalse(result.isPresent());
    }

    @Test //There are 2 books which has 'tale' in their title
    public void testNoCursorFindBooks() {
            List<Book> books = bookRepository.noCursorFindBooks("tale", 1800, 5);
        assertEquals(2, books.size());
    }

    @Test //This method of Repository, takes into account the cursor as well to find the books when the user
          // clicks on the next page, since there are 20 books, it is very much within the total, so 5 books are provided
    public void testNextPageFindBooks() {
        List<Book> books = bookRepository.nextPageFindBooks(11L, 5, "", 1900);
        assertEquals(5, books.size());
    }

    @Test //This method of Repository, takes into account the cursor as well to find the books when the user
    // clicks on the next page, since there are 20 books, and cursor is 17, so 4 books will be returned as only 4 left.
    public void testNextPageFindBooksBeyondAvailable() {
        List<Book> books = bookRepository.nextPageFindBooks(17L, 5, "", 0);
        assertEquals(4, books.size());
    }

    @Test //This method in the Repository, finds the books on the previous from the current cursor, the cursor
    //is not taken into account, so for cursorId at 12, there are enough books within limit, so 5 will be fetched
    public void testPreviousPageFindBooks() {
        List<Book> books = bookRepository.previousPageFindBooks(12L, 5, "", 0);
        assertEquals(5, books.size());
    }

    @Test //This method in the Repository, finds the books on the previous from the current cursor, the cursor
    //is not taken into account, so for cursorId at 8, there are 7 books left and limit is 10, so 7 will be fetched
    public void testPreviousPageFindBooksGreaterLimit() {
        List<Book> books = bookRepository.previousPageFindBooks(8L, 10, "", 0);
        assertEquals(7, books.size());
    }


}
