package de.anevis.backend.domain;

import de.anevis.backend.pagination.Cursors;

public record ReceiverBookDTO (
        Book book,
        Cursors cursor,
        int limit,
        String direction
){
    @Override
    public Book book() {
        return book;
    }

    @Override
    public Cursors cursor() {
        return cursor;
    }

    @Override
    public int limit() {
        return limit;
    }

    @Override
    public String direction() {
        return direction;
    }


}

