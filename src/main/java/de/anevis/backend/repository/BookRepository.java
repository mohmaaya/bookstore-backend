package de.anevis.backend.repository;

import de.anevis.backend.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT CASE WHEN COUNT(b) = 1 THEN true ELSE false END FROM Book b WHERE b.title = ?1 AND b.authorName = ?2")
    boolean selectBookExists(String title, String author);
}

