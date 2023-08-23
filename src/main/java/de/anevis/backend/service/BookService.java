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

	public SenderBookDTO findAllBooks(){
		int initialLimit = 10;
		long initialCursor = -1;
		String initialDirection = "";
		Pages pages = currentPageStrategy.calculatePagination(initialCursor, initialLimit);
		System.out.println(pages.getCurrentPageBooks());
		Cursors cursors = cursorManager.calculateCursors(pages, 10, "");
		return senderBookDTOMapper.apply(pages.getCurrentPageBooks(),
				cursors,
				initialLimit,
				initialDirection);
	}

	public SenderBookDTO findBooksPage(String currentCursor, int limit, String direction) {
		Long cursorId = CursorEncode.decodeCursor(currentCursor);
		PaginationStrategy paginationStrategy = PaginationDirection.NEXT_PAGE.toString().equals(direction) ?
				nextPageStrategy : previousPageStrategy;

		Pages pages = paginationStrategy.calculatePagination(cursorId, limit);
		Cursors cursors = cursorManager.calculateCursors(pages, limit, currentCursor);
		return senderBookDTOMapper.apply(pages.getCurrentPageBooks(), cursors, limit, direction);

	}

	public SenderBookDTO addBook(ReceiverBookDTO receiverBookDTO)  {
		boolean bookExists = bookRepository
				.selectBookExists(receiverBookDTO.book().getTitle(), receiverBookDTO.book().getAuthorName());
		if (bookExists) {
			throw new DuplicateBookException("Book with same title " + receiverBookDTO.book().getTitle() + " and author " + receiverBookDTO.book().getAuthorName() + " present");
		}
		bookRepository.save(receiverBookDTO.book());

		Long cursorId = receiverBookDTO.cursor().getCurrentCursor().isEmpty() ?
				-1 :
				CursorEncode.decodeCursor(receiverBookDTO.cursor().getCurrentCursor());

		PaginationStrategy paginationStrategy;

		if(receiverBookDTO.direction().equals(PaginationDirection.NEXT_PAGE.toString()))
			paginationStrategy = nextPageStrategy;
		else if(receiverBookDTO.direction().equals(PaginationDirection.PREVIOUS_PAGE.toString()))
			paginationStrategy = previousPageStrategy;
		else paginationStrategy = currentPageStrategy;

		Pages pages = paginationStrategy.calculatePagination(cursorId, receiverBookDTO.limit());
		Cursors cursors = cursorManager.calculateCursors(pages, receiverBookDTO.limit(), receiverBookDTO.cursor().getCurrentCursor());
		return senderBookDTOMapper.apply(pages.getCurrentPageBooks(), cursors, receiverBookDTO.limit(), receiverBookDTO.direction());

	}

	public SenderBookDTO updateBook(Long bookId, ReceiverBookDTO receiverBookDTO) {
		Optional<Book> toUpdateBook = bookRepository.findById(bookId);
		Optional<Boolean> duplicateBook = Optional.of(bookRepository.selectBookExists(receiverBookDTO.book().getTitle(), receiverBookDTO.book().getAuthorName()));
		if (toUpdateBook.isPresent() && !duplicateBook.orElse(false)) {
			Book existingBook = toUpdateBook.get();
			existingBook.setTitle(receiverBookDTO.book().getTitle());
			existingBook.setAuthorName(receiverBookDTO.book().getAuthorName());
			existingBook.setFirstPublishYear(receiverBookDTO.book().getFirstPublishYear());
			existingBook.setNumberOfPagesMedian(receiverBookDTO.book().getNumberOfPagesMedian());
			existingBook.setCovers(receiverBookDTO.book().getCovers());

			bookRepository.save(existingBook);
			Long cursorId = receiverBookDTO.cursor().getCurrentCursor().isEmpty() ?
					-1 :
					CursorEncode.decodeCursor(receiverBookDTO.cursor().getCurrentCursor());

			PaginationStrategy paginationStrategy;

			if(receiverBookDTO.direction().equals(PaginationDirection.NEXT_PAGE.toString()))
				paginationStrategy = nextPageStrategy;
			else if(receiverBookDTO.direction().equals(PaginationDirection.PREVIOUS_PAGE.toString()))
				paginationStrategy = previousPageStrategy;
			else paginationStrategy = currentPageStrategy;

			Pages pages = paginationStrategy.calculatePagination(cursorId, receiverBookDTO.limit());
			Cursors cursors = cursorManager.calculateCursors(pages, receiverBookDTO.limit(), receiverBookDTO.cursor().getCurrentCursor());
			return senderBookDTOMapper.apply(pages.getCurrentPageBooks(), cursors, receiverBookDTO.limit(), receiverBookDTO.direction());

		}
			throw new BookNotFoundException("Book with id: " + bookId + " not found");
	}

	public SenderBookDTO deleteBook(Long bookId, String cursor, int limit, String direction) {

		if(!bookRepository.existsById(bookId)) {
			throw new BookNotFoundException("Book with id: " + bookId + " not found");
		}

		 bookRepository.deleteById(bookId);

		Long cursorId = cursor.isEmpty() ?
				-1 :
				CursorEncode.decodeCursor(cursor);

		PaginationStrategy paginationStrategy;

		if(PaginationDirection.NEXT_PAGE.toString().equals(direction))
			paginationStrategy = nextPageStrategy;
		else if(PaginationDirection.PREVIOUS_PAGE.toString().equals(direction))
			paginationStrategy = previousPageStrategy;
		else paginationStrategy = currentPageStrategy;

		Pages pages = paginationStrategy.calculatePagination(cursorId, limit);
		Cursors cursors = cursorManager.calculateCursors(pages, limit, cursor);
		return senderBookDTOMapper.apply(pages.getCurrentPageBooks(), cursors, limit, direction);
	}
}
