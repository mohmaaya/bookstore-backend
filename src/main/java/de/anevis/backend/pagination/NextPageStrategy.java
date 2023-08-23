package de.anevis.backend.pagination;

import de.anevis.backend.domain.Book;
import de.anevis.backend.repository.BookRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NextPageStrategy implements PaginationStrategy{

    private final BookRepository bookRepository;

    public NextPageStrategy(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public Pages calculatePagination(Long cursorId, int limit) {
        List<Book> currentPageBooks = null;
        List<Book> nextPageBooks = null;
        List<Book> previousPageBooks = null;


        nextPageBooks = bookRepository.nextPageFindBooks(cursorId, limit + 1);
        currentPageBooks = nextPageBooks.isEmpty() ? bookRepository.previousPageFindBooks(cursorId, limit) :
                           bookRepository.nextPageFindBooks(cursorId, limit);
        previousPageBooks = bookRepository.previousPageFindBooks(currentPageBooks.get(0).getId(), limit);
        return new Pages(currentPageBooks, nextPageBooks, previousPageBooks);
    }
}
    //Long currentPageCursorId = !currentPageBooks.isEmpty() ? currentPageBooks.get(0).getId(): 0;