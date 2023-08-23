package de.anevis.backend.repository;

import de.anevis.backend.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT CASE WHEN COUNT(b) = 1 THEN true ELSE false END FROM Book b WHERE b.title = ?1 AND b.authorName = ?2")
    boolean selectBookExists(String title, String author);

    @Query(value = "SELECT * FROM Books LIMIT 11", nativeQuery = true)
    List<Book> noCursorFindBooks();

    @Query(value = "SELECT * FROM (SELECT * FROM Books b WHERE b.id < ?1 ORDER BY b.id DESC LIMIT ?2) AS subquery ORDER BY id ASC", nativeQuery = true)
    List<Book> previousPageFindBooks(long cursorId, int limit);

    @Query(value = "SELECT * FROM Books b WHERE b.id >= ?1 LIMIT ?2", nativeQuery = true)
    List<Book> nextPageFindBooks(long cursorId, int limit);

}