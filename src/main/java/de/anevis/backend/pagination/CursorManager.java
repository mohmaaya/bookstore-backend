package de.anevis.backend.pagination;

import org.springframework.stereotype.Component;

@Component
public class CursorManager {

    public Cursors calculateCursors(Pages pages, int limit, String currentCursor){

        String nextPageCursor = "";
        String previousPageCursor = "";
        if (pages.getNextPageBooks().isEmpty() &&
                pages.getPreviousPageBooks().isEmpty() &&
                pages.getCurrentPageBooks().size() > limit) {
            nextPageCursor = CursorEncode.encodeId(pages.getCurrentPageBooks().
                               get(pages.getCurrentPageBooks().size() - 1).getId());
            pages.getCurrentPageBooks().remove(pages.getCurrentPageBooks().size()-1);


        }


        if (!pages.getNextPageBooks().isEmpty() &&
                pages.getNextPageBooks().size() >= limit + 1) {
            nextPageCursor = CursorEncode.encodeId(pages.getNextPageBooks().get(limit).getId());
        }

        if (!pages.getPreviousPageBooks().isEmpty()) {
            previousPageCursor = CursorEncode.encodeId(pages.getCurrentPageBooks().get(0).getId());
        }

        return new Cursors(nextPageCursor, previousPageCursor, currentCursor);
    }
}

