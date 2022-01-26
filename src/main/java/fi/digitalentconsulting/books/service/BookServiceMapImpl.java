package fi.digitalentconsulting.books.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.digitalentconsulting.books.model.dto.BookTO;

public class BookServiceMapImpl implements BookService {
	private static Logger LOGGER = LoggerFactory.getLogger(BookServiceMapImpl.class);
	private Map<Long, BookTO> books = new HashMap<>();
	private AtomicLong nextId = new AtomicLong(1);

	@Override
	public List<BookTO> findBooks() {
		return new ArrayList<BookTO>(books.values());
	}

	@Override
	public Optional<BookTO> findBook(Long id) {
		BookTO book = books.get(id);
		if (book == null) {
			return Optional.empty();
		}
		return Optional.of(book);
	}

	@Override
	public BookTO add(BookTO book) {
		book.setId(nextId.getAndIncrement());
		books.put(book.getId(), book);
		return book;
	}

	@Override
	public void delete(BookTO book) {
		books.remove(book.getId());
	}

	@Override
	public BookTO modify(Long id, BookTO newValues) {
		BookTO book = books.get(id);
		if (book == null) {
			LOGGER.warn("Trying to modify nonexisting book with id {}, and values {}", id, newValues);
			throw new NoSuchElementException("No book with id: " + id);
		}
		book.setAuthor(newValues.getAuthor());
		book.setCategories(newValues.getCategories());
		book.setIsbn(newValues.getIsbn());
		book.setName(newValues.getName());
		return book;
	}

	@Override
	public void addAll(Collection<BookTO> books) {
		books.forEach(this::add);
	}
	
	public long nextId() {
		return nextId.get();
	}
}
