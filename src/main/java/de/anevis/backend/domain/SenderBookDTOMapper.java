package de.anevis.backend.domain;

import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class SenderBookDTOMapper implements CustomQuadFunction<List<Book>, Integer, List<Book> , List<Book>, SenderBookDTO> {


    @Override
    public SenderBookDTO apply(List<Book> books, Integer limit, List<Book> nextPageBooks, List<Book> prevPageBooks) {

        String nextPageCursor = "";
        String previousPageCursor = "";
        if(nextPageBooks.isEmpty() && prevPageBooks.isEmpty() && books.size() > limit){
            nextPageCursor = CursorEncode.encodeId(books.get(books.size() - 1).getId());
            books.remove(books.size() - 1);
        }
        if (!nextPageBooks.isEmpty() &&
                nextPageBooks.size() > limit) {
             nextPageCursor = CursorEncode.encodeId(nextPageBooks.get(nextPageBooks.size() - 1).getId());
            books.remove(books.size() - 1);

        }
        if(!prevPageBooks.isEmpty() && prevPageBooks.size() > limit){
            previousPageCursor = CursorEncode.encodeId(prevPageBooks.get(0).getId());
        }

        return new SenderBookDTO(books, new Cursors(nextPageCursor, previousPageCursor));
    }
    }

