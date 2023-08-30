package de.anevis.backend.pagination;

import de.anevis.backend.domain.Book;
import de.anevis.backend.repository.BookRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class PreviousPageStrategy implements PaginationStrategy {

    private final BookRepository bookRepository;

    public PreviousPageStrategy(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Pages calculatePagination(Long cursorId,
                                     Integer limit,
                                     String title,
                                     Integer year) {
        List<Book> currentPageBooks = Collections.emptyList();
        List<Book> nextPageBooks = Collections.emptyList();
        List<Book> previousPageBooks = Collections.emptyList();
        Integer booksAvailable = 0;

        currentPageBooks = bookRepository.previousPageFindBooks(cursorId, limit, title, year);
        if (!currentPageBooks.isEmpty()) {
            nextPageBooks = bookRepository.nextPageFindBooks(currentPageBooks.get(0).getId(), limit + 1, title, year);
            previousPageBooks = bookRepository.previousPageFindBooks(currentPageBooks.get(0).getId(), limit, title, year);
            booksAvailable = bookRepository.booksAvailable(currentPageBooks.get(currentPageBooks.size() - 1).getId(), limit, title, year);

        }
        return new Pages(currentPageBooks, nextPageBooks, previousPageBooks, booksAvailable);
    }
}
