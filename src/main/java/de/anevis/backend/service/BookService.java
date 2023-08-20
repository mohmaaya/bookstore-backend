package de.anevis.backend.service;

import de.anevis.backend.exception.BookNotFoundException;
import de.anevis.backend.exception.DuplicateBookException;
import de.anevis.backend.repository.BookRepository;
import de.anevis.backend.domain.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

	private final BookRepository bookRepository;

	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	public List<Book> findAll() {
		return bookRepository.findAll();
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
