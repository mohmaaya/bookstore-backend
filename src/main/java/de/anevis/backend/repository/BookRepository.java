package de.anevis.backend.repository;

import de.anevis.backend.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT CASE WHEN COUNT(b) = 1 THEN true ELSE false END FROM Book b WHERE b.title = ?1 AND b.authorName = ?2")
    boolean selectBookExists(String title, String author);

    @Query("SELECT b From Book b WHERE b.title = ?1 AND b.authorName = ?2")
    Optional<Book> findBook(String title, String author);

    @Query(value = "SELECT * FROM Books b " +
            "WHERE (?1 IS NULL OR LOWER(b.title) LIKE CONCAT('%', LOWER(?1), '%')) " +
            "AND (?2 IS NULL OR b.first_publish_year >= ?2) " +
            "LIMIT ?3", nativeQuery = true)
    List<Book> noCursorFindBooks(String title, Integer year, Integer limit);

    @Query(value = "SELECT * FROM Books b " +
            "WHERE b.id >= ?1 " +
            "AND (?3 IS NULL OR LOWER(b.title) LIKE CONCAT('%', LOWER(?3), '%')) " +
            "AND (?4 IS NULL OR b.first_publish_year >= ?4) " +
            "LIMIT ?2", nativeQuery = true)
    List<Book> nextPageFindBooks(Long cursorId, Integer limit, String title, Integer year);

    @Query(value = "SELECT * FROM (SELECT * FROM Books b " +
            "WHERE b.id < ?1 " +
            "AND (?3 IS NULL OR LOWER(b.title) LIKE CONCAT('%', LOWER(?3), '%')) " +
            "AND (?4 IS NULL OR b.first_publish_year >= ?4) " +
            "ORDER BY b.id DESC LIMIT ?2) AS subquery " +
            "ORDER BY id ASC", nativeQuery = true)
    List<Book> previousPageFindBooks(Long cursorId, Integer limit, String title, Integer year);

    @Query(value = "SELECT Count(*) FROM Books b " +
            "WHERE b.id > ?1 " +
            "AND (?3 IS NULL OR LOWER(b.title) LIKE CONCAT('%', LOWER(?3), '%')) " +
            "AND (?4 IS NULL OR b.first_publish_year >= ?4) " +
            "LIMIT ?2", nativeQuery = true)
    Integer booksAvailable(Long cursorId, Integer limit, String title, Integer year);

}


