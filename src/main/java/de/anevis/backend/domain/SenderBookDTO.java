package de.anevis.backend.domain;


import de.anevis.backend.pagination.Cursors;

import java.util.List;
public record SenderBookDTO (
    List<Book> books,
    Cursors cursor,
    int limit,
    String direction
    ){ }
