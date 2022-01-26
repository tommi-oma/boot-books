package fi.digitalentconsulting.books.service;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import fi.digitalentconsulting.books.model.dto.BookTO;

public interface BookService {
	/**
	 * Finds and return all the books in the service.
	 * @return All books
	 */
	public List<BookTO> findBooks();
	/**
	 * Finds a single book in the service, and returns it embedded in an 
	 * Optional.
	 * @param id Book id
	 * @return The book in an Optional, or Optional.empty() if not found
	 */
	public Optional<BookTO> findBook(Long id);
	/**
	 * Adds a new book to the service. An id is generated for the new book.
	 * @param book with values that are added to the book stored. 
	 * Possible id is ignored.
	 * @return The book that was created, with the generated id.
	 */
	public BookTO add(BookTO book);
	/**
	 * Deletes the book from the service. No-op if the book does not exist.
	 * @param book Only the id field is considered when finding and deleting
	 * the book
	 */
	public void delete(BookTO book);
	/**
	 * Modifies the values for a book.
	 * @param id The id of the book
	 * @param newValues <em>All</em> other values except id are copied to the
	 * existing book
	 * @return The modified book
	 * @throws NoSuchElementException if a book with the passed id is not 
	 * found.
	 */
	public BookTO modify(Long id, BookTO newValues) 
			throws NoSuchElementException;
	
	public void addAll(Collection<BookTO> books);
}
