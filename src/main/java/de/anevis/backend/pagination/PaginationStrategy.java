package de.anevis.backend.pagination;

import de.anevis.backend.domain.Book;

import java.util.List;

public interface PaginationStrategy {

    Pages calculatePagination(Long cursorId, int limit);
 }
