package de.anevis.backend.pagination;

import de.anevis.backend.domain.Book;

import java.util.List;

public class Pages {

    private List<Book> currentPageBooks;
    private List<Book> nextPageBooks;
    private List<Book> previousPageBooks;

    public Pages(List<Book> currentPageBooks, List<Book> nextPageBooks, List<Book> previousPageBooks){
        this.currentPageBooks = currentPageBooks;
        this.nextPageBooks = nextPageBooks;
        this.previousPageBooks = previousPageBooks;
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
}
