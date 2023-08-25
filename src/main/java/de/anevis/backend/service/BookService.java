package de.anevis.backend.service;

import de.anevis.backend.domain.*;
import de.anevis.backend.exception.BookNotFoundException;
import de.anevis.backend.exception.DuplicateBookException;
import de.anevis.backend.pagination.*;
import de.anevis.backend.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class BookService {

	private final BookRepository bookRepository;
	private final SenderBookDTOMapper senderBookDTOMapper;
	private final NextPageStrategy nextPageStrategy;
	private final PreviousPageStrategy previousPageStrategy;
	private final CurrentPageStrategy currentPageStrategy;
	private final CursorManager cursorManager;

	public BookService(BookRepository bookRepository,
					   SenderBookDTOMapper senderBookDTOMapper,
					   NextPageStrategy nextPageStrategy,
					   PreviousPageStrategy previousPageStrategy,
					   CurrentPageStrategy currentPageStrategy,
					   CursorManager cursorManager) {
		this.bookRepository = bookRepository;
		this.senderBookDTOMapper = senderBookDTOMapper;
		this.nextPageStrategy = nextPageStrategy;
		this.previousPageStrategy = previousPageStrategy;
		this.currentPageStrategy = currentPageStrategy;
		this.cursorManager = cursorManager;
	}

	public SenderBookDTO findAllBooks(String cursor, Integer limit, String direction, String title, Integer year) {
		Long cursorId = cursor.isEmpty() ?
				null :
				CursorEncode.decodeCursor(cursor);

		PaginationStrategy paginationStrategy;

		if (PaginationDirection.NEXT_PAGE.toString().equals(direction))
			paginationStrategy = nextPageStrategy;
		else if (PaginationDirection.PREVIOUS_PAGE.toString().equals(direction))
			paginationStrategy = previousPageStrategy;
		else paginationStrategy = currentPageStrategy;

		Pages pages = paginationStrategy.calculatePagination(cursorId, limit, title, year);
		Cursors cursors = cursorManager.calculateCursors(pages, limit, cursor);
		return senderBookDTOMapper.apply(pages.getCurrentPageBooks(),
				cursors,
				limit,
				direction);
	}

	public void addBook(Book book) {
		boolean bookExists = bookRepository
				.selectBookExists(book.getTitle(), book.getAuthorName());
		if (bookExists) {
			throw new DuplicateBookException("Book with same title " + book.getTitle() + " and author " + book.getAuthorName() + " present");
		}
		bookRepository.save(book);
	}

	public void updateBook(Long bookId, Book book) {
		Optional<Book> toUpdateBook = bookRepository.findById(bookId);
		Optional<Boolean> duplicateBook = Optional.of(bookRepository.selectBookExists(book.getTitle(), book.getAuthorName()));
		if (toUpdateBook.isPresent() && !duplicateBook.orElse(false)) {
			Book existingBook = toUpdateBook.get();
			existingBook.setTitle(book.getTitle());
			existingBook.setAuthorName(book.getAuthorName());
			existingBook.setFirstPublishYear(book.getFirstPublishYear());
			existingBook.setNumberOfPagesMedian(book.getNumberOfPagesMedian());
			existingBook.setCovers(book.getCovers());

			bookRepository.save(existingBook);
		}
		else throw new BookNotFoundException("Book with id: " + bookId + " not found");
	}

	public void deleteBook(Long bookId) {

		if (!bookRepository.existsById(bookId)) {
			throw new BookNotFoundException("Book with id: " + bookId + " not found");
		}

		bookRepository.deleteById(bookId);

	}
}
