package de.anevis.backend.domain;


import de.anevis.backend.pagination.Cursors;

import java.util.List;
import java.util.Optional;

public record SenderBookDTO (
        List<Book> books,
        Cursors cursor,
        Integer limit,
        String direction
    ){ }
