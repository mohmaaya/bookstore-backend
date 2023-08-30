package de.anevis.backend.domain;

import de.anevis.backend.pagination.Cursors;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class SenderBookDTOMapper implements CustomFunction<
            List<Book>,
            Cursors,
            Integer,
            String,
            SenderBookDTO> {
    @Override
    public SenderBookDTO apply(List<Book> currentPageBooks,
                               Cursors cursors,
                               Integer limit,
                               String direction) {


        return new SenderBookDTO(currentPageBooks,cursors, limit, direction);
    }
    }

