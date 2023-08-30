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
    public Pages calculatePagination( Long cursorId,
                                      Integer limit,
                                      String title,
                                      Integer year) {
        List<Book> currentPageBooks;

        currentPageBooks = bookRepository.noCursorFindBooks(title, year, limit+1);
        return new Pages(currentPageBooks, Collections.emptyList(), Collections.emptyList(), 0);
    }
}