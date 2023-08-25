package de.anevis.backend.pagination;

import de.anevis.backend.domain.Book;
import de.anevis.backend.repository.BookRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class NextPageStrategy implements PaginationStrategy{

    private final BookRepository bookRepository;

    public NextPageStrategy(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public Pages calculatePagination(Long cursorId,
                                     Integer limit,
                                     String title,
                                     Integer year) {
        List<Book> currentPageBooks = null;
        List<Book> nextPageBooks = null;
        List<Book> previousPageBooks = null;

        nextPageBooks = bookRepository.nextPageFindBooks(cursorId, limit+1, title, year);
        currentPageBooks = nextPageBooks.isEmpty() ? bookRepository.previousPageFindBooks(cursorId, limit, title, year) :
                           bookRepository.nextPageFindBooks(cursorId, limit, title, year);
        previousPageBooks = bookRepository.previousPageFindBooks(currentPageBooks.get(0).getId(), limit, title, year);
        return new Pages(currentPageBooks, nextPageBooks, previousPageBooks);
    }
}
