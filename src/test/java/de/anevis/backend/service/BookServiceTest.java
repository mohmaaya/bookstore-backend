package de.anevis.backend.service;

import de.anevis.backend.domain.Book;
import de.anevis.backend.domain.SenderBookDTO;
import de.anevis.backend.domain.SenderBookDTOMapper;
import de.anevis.backend.exception.BookNotFoundException;
import de.anevis.backend.exception.DuplicateBookException;
import de.anevis.backend.pagination.*;
import de.anevis.backend.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookServiceTester;

    @Mock
    private CurrentPageStrategy currentPageStrategy;

    @Mock
    private CursorManager cursorManager;

    @Mock
    private SenderBookDTOMapper senderBookDTOMapper;

    @Mock
    private BookRepository bookRepository;

    @Test
    public void testFindAllBooksForNoDirection() {

        String direction = "";
        Integer limit = 10;
        String cursor = "";

        Cursors cursors = new Cursors("", "", cursor);
        Pages pages = new Pages(new ArrayList<>(), new ArrayList<>() , new ArrayList<>(), 0);

        when(cursorManager.calculateCursors(any(), any(), any())).thenReturn(cursors);
        when(currentPageStrategy.calculatePagination(any(), any(), any(), any())).thenReturn(pages);

        SenderBookDTO expectedDto = new SenderBookDTO(new ArrayList<>(), cursors, limit, "");
        when(senderBookDTOMapper.apply(any(), any(), any(), any())).thenReturn(expectedDto);

        SenderBookDTO resultDto = bookServiceTester.findAllBooks(cursor, limit, direction, "", 0);

        assertEquals(expectedDto, resultDto);
    }



    @Test
    public void testAddBook_Success() {
        Book book = new Book();
        book.setTitle("Test Title");
        book.setAuthorName("Test Author");

        when(bookRepository.selectBookExists(book.getTitle(), book.getAuthorName())).thenReturn(false);

        bookServiceTester.addBook(book);

        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void testAddBook_DuplicateBook() {
        Book book = new Book();
        book.setTitle("Test Title");
        book.setAuthorName("Test Author");

        when(bookRepository.selectBookExists(book.getTitle(), book.getAuthorName())).thenReturn(true);

        assertThrows(DuplicateBookException.class, () -> bookServiceTester.addBook(book));

        verify(bookRepository, never()).save(any());
    }
    @Test
    public void testUpdateBook_Success() {
        Long bookId = 1L;
        Book existingBook = new Book();
        existingBook.setTitle("Existing Title");
        existingBook.setAuthorName("Existing Author");

        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Title");
        updatedBook.setAuthorName("Updated Author");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.findBook(updatedBook.getTitle(), updatedBook.getAuthorName())).thenReturn(Optional.empty());
        when(bookRepository.save(any())).thenReturn(updatedBook);

        Book resultBook = bookServiceTester.updateBook(bookId, updatedBook);

        assertEquals(updatedBook.getTitle(), resultBook.getTitle());
        assertEquals(updatedBook.getAuthorName(), resultBook.getAuthorName());
        assertEquals(existingBook.getFirstPublishYear(), resultBook.getFirstPublishYear());
        assertEquals(existingBook.getNumberOfPagesMedian(), resultBook.getNumberOfPagesMedian());
        assertEquals(existingBook.getCovers(), resultBook.getCovers());

        verify(bookRepository, times(1)).save(any());
    }


    @Test
    public void testUpdateBook_NotFound() {
        Long bookId = 1L;
        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Title");
        updatedBook.setAuthorName("Updated Author");

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookServiceTester.updateBook(bookId, updatedBook));
        verify(bookRepository, never()).save(any());
    }
    @Test
    public void testDeleteBook_BookFound() {
        Long bookId = 1L;

        when(bookRepository.existsById(Mockito.eq(bookId))).thenReturn(true);

        bookServiceTester.deleteBook(bookId);

        verify(bookRepository).deleteById(Mockito.eq(bookId));
    }

    @Test
    public void testDeleteBook_BookNotFound() {
        Long bookId = 1L;

        when(bookRepository.existsById(Mockito.eq(bookId))).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookServiceTester.deleteBook(bookId));
        verify(bookRepository, never()).deleteById(anyLong());
    }

}




