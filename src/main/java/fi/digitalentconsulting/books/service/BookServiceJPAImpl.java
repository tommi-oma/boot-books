package fi.digitalentconsulting.books.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import fi.digitalentconsulting.books.entity.Book;
import fi.digitalentconsulting.books.model.dto.BookTO;
import fi.digitalentconsulting.books.repository.BookRepository;
import org.springframework.transaction.annotation.Transactional;

public class BookServiceJPAImpl implements BookService {

	private BookRepository bookRepository;
	
	public BookServiceJPAImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	public List<BookTO> findBooks() {
		List<Book> books = new ArrayList<>();
		bookRepository.findAll().forEach(books::add);
		return books.stream()
				.map(book -> new BookTO(book))
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public Optional<BookTO> findBook(Long id) {
		Optional<Book> optbook = bookRepository.findById(id);
		if (optbook.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(new BookTO(optbook.get()));
	}

	@Override
	public BookTO add(BookTO bookto) {
		Book book = bookto.convertToBook();
		book = bookRepository.save(book);
		return new BookTO(book);
	}

	@Override
	public void delete(BookTO book) {
		Book ent = book.convertToBook();
		bookRepository.delete(ent);
	}

	@Override
	public BookTO modify(Long id, BookTO newValues) throws NoSuchElementException {
		Book toModify = bookRepository.findById(id).get();
		newValues.copyTo(toModify);
		bookRepository.save(toModify);
		return new BookTO(toModify);
	}

	@Override
	public void addAll(Collection<BookTO> books) {
		books.stream()
			.map(BookTO::convertToBook)
			.forEach(bookRepository::save);
	}
}
