package de.anevis.backend.domain;

import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class SenderBookDTOMapper implements CustomFunction<List<Book>, Integer, List<Book> , List<Book>, SenderBookDTO> {


    @Override
    public SenderBookDTO apply(List<Book> books, Integer limit, List<Book> nextPageBooks, List<Book> prevPageBooks) {

        String nextPageCursor = "";
        String previousPageCursor = "";


        if(nextPageBooks.isEmpty() && prevPageBooks.isEmpty() && books.size() > limit){
            nextPageCursor = CursorEncode.encodeId(books.get(books.size() - 1).getId());
            books.remove(books.size()-1);
        }
        if (!nextPageBooks.isEmpty() && nextPageBooks.size() >= limit + 1) {
            nextPageCursor = CursorEncode.encodeId(nextPageBooks.get(limit).getId());
        }

        if(!prevPageBooks.isEmpty()){
            previousPageCursor = CursorEncode.encodeId(books.get(0).getId());
        }

        return new SenderBookDTO(books, new Cursors(nextPageCursor, previousPageCursor));
    }
    }

