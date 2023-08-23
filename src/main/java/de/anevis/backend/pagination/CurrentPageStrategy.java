package de.anevis.backend.pagination;

import de.anevis.backend.domain.Book;
import de.anevis.backend.repository.BookRepository;
import org.springframework.stereotype.Component;


import java.util.Collections;
import java.util.List;
@Component
public class CurrentPageStrategy implements PaginationStrategy {

    private final BookRepository bookRepository;

    public CurrentPageStrategy(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @Override
    public Pages calculatePagination(Long cursorId, int limit) {
        List<Book> currentPageBooks;
        List<Book> nextPageBooks = Collections.emptyList();
        List<Book> previousPageBooks = Collections.emptyList();
        currentPageBooks = bookRepository.noCursorFindBooks(limit+1);
        return new Pages(currentPageBooks, nextPageBooks, previousPageBooks);
    }
}