package de.anevis.backend.pagination;

import de.anevis.backend.domain.Book;

import java.util.List;

public class Pages {

    private final List<Book> currentPageBooks;
    private final List<Book> nextPageBooks;
    private final List<Book> previousPageBooks;
    private final Integer booksAvailable;


    public Pages(List<Book> currentPageBooks,
                 List<Book> nextPageBooks,
                 List<Book> previousPageBooks,
                 Integer booksAvailable){
        this.currentPageBooks = currentPageBooks;
        this.nextPageBooks = nextPageBooks;
        this.previousPageBooks = previousPageBooks;
        this.booksAvailable = booksAvailable;
    }

    public List<Book> getCurrentPageBooks() {
        return currentPageBooks;
    }
    public List<Book> getNextPageBooks() {
        return nextPageBooks;
    }

    public List<Book> getPreviousPageBooks() {
        return previousPageBooks;
    }
    public Integer getBooksAvailable() {
        return booksAvailable;
    }
}
