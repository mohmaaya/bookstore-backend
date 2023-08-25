package de.anevis.backend.pagination;

import de.anevis.backend.domain.Book;

import java.util.List;
import java.util.Optional;

public interface PaginationStrategy {

    Pages calculatePagination(
            Long cursorId,
            Integer limit,
            String title,
            Integer year);

}
