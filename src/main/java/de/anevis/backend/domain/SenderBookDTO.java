package de.anevis.backend.domain;


import java.util.List;
public record SenderBookDTO (
    List<Book> books,
    Cursors cursor
    ){ }
