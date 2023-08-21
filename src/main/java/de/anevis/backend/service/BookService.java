package de.anevis.backend.service;

import de.anevis.backend.domain.*;
import de.anevis.backend.exception.BookNotFoundException;
import de.anevis.backend.exception.DuplicateBookException;
import de.anevis.backend.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

	private final BookRepository bookRepository;
	private final SenderBookDTOMapper senderBookDTOMapper;

	public BookService(BookRepository bookRepository, SenderBookDTOMapper senderBookDTOMapper) {
		this.bookRepository = bookRepository;
		this.senderBookDTOMapper = senderBookDTOMapper;
	}

	public SenderBookDTO findAllBooks(){
		List<Book> books = bookRepository.noCursorFindBooks();
		return senderBookDTOMapper.apply(books,10, Collections.emptyList(), Collections.emptyList());
	}

	public SenderBookDTO findBooksNextPage(String nextCursor, int limit){
		long cursorId = CursorEncode.decodeCursor(nextCursor);
		List<Book> nextPageBooks = bookRepository.nextPageFindBooks(cursorId, limit+1);
		List<Book> previousPageBooks = bookRepository.previousPageFindBooks(cursorId,limit);
		return senderBookDTOMapper.apply(nextPageBooks, limit, nextPageBooks, previousPageBooks);
	}

	public SenderBookDTO findBooksPreviousPage(String nextCursor, int limit){
		long cursorId = CursorEncode.decodeCursor(nextCursor);
		List<Book> nextPageBooks = bookRepository.nextPageFindBooks(cursorId, limit+1);
		List<Book> previousPageBooks = bookRepository.previousPageFindBooks(cursorId,limit);
		return senderBookDTOMapper.apply(previousPageBooks, limit, nextPageBooks, previousPageBooks);
	}

	public Book updateBook(Long bookId, Book updatedBook) {
		Optional<Book> optionalBook = bookRepository.findById(bookId);

		if (optionalBook.isPresent()) {
			Book existingBook = optionalBook.get();
			existingBook.setTitle(updatedBook.getTitle());
			existingBook.setAuthorName(updatedBook.getAuthorName());
			existingBook.setFirstPublishYear(updatedBook.getFirstPublishYear());
			existingBook.setNumberOfPagesMedian(updatedBook.getNumberOfPagesMedian());
			existingBook.setCovers(updatedBook.getCovers());

			return bookRepository.save(existingBook);
		}
			throw new BookNotFoundException("Book with id: " + bookId + " not found");

	}

	public Book addBook(Book book)  {
		boolean bookExists = bookRepository
				.selectBookExists(book.getTitle(), book.getAuthorName());
		if (bookExists) {
			throw new DuplicateBookException("Book with same title " + book.getTitle() + " and author " + book.getAuthorName() + " present");
		}
		return bookRepository.save(book);
	}

	public void deleteBook(Long bookId) {
		if(!bookRepository.existsById(bookId)) {
			throw new BookNotFoundException("Book with id: " + bookId + " not found");
		}
		 bookRepository.deleteById(bookId);
	}
}
