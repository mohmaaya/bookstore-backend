package de.anevis.backend.pagination;

import de.anevis.backend.domain.Book;
import de.anevis.backend.repository.BookRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PreviousPageStrategy implements PaginationStrategy{

    private final BookRepository bookRepository;

    public PreviousPageStrategy(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }



    @Override
    public Pages calculatePagination(Long cursorId, int limit) {
        List<Book> currentPageBooks = null;
        List<Book> nextPageBooks = null;
        List<Book> previousPageBooks = null;
        currentPageBooks = bookRepository.previousPageFindBooks(cursorId, limit);
        if(!currentPageBooks.isEmpty()) {
            nextPageBooks = bookRepository.nextPageFindBooks(currentPageBooks.get(0).getId(), limit + 1);
            previousPageBooks = bookRepository.previousPageFindBooks(currentPageBooks.get(0).getId(), limit);
        }
        return new Pages(currentPageBooks, nextPageBooks, previousPageBooks);
    }
}
