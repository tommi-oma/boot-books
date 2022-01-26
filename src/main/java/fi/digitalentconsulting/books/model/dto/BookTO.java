package fi.digitalentconsulting.books.model.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

import fi.digitalentconsulting.books.entity.Book;
import fi.digitalentconsulting.books.entity.Publisher;


public class BookTO {
	private Long id;
	@NotBlank
	private String name;
	@NotBlank
	private String author;
	private List<Category> categories = new ArrayList<>();
	private String isbn;
	private Publisher publisher;
	
	public BookTO() {
	}

	public BookTO(String name, String author, Collection<Category> categories, String isbn) {
		this.name = name;
		this.author = author;
		if (categories != null && !categories.isEmpty())
			this.categories.addAll(categories);
		this.isbn = isbn;
	}
	
	public BookTO(String name, String author, Collection<Category> categories, String isbn, Publisher publisher) {
		this.name = name;
		this.author = author;
		if (categories != null && !categories.isEmpty())
			this.categories.addAll(categories);
		this.isbn = isbn;
		this.publisher = publisher;
	}
	
	public BookTO(Book book) {
		id = book.getId();
		name = book.getTitle();
		author = book.getAuthor();
		categories.clear();
		categories.addAll(book.getCategories());
		isbn = book.getIsbn();
		publisher = book.getPublisher();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public List<Category> getCategories() {
		return categories;
	}
	
	public void setCategories(Collection<Category> categories) {
		this.categories.clear();
		this.categories.addAll(categories);
	}

	public void addCategory(Category category) {
		this.categories.add(category);
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}


	@Override
	public String toString() {
		return "BookTO [id=" + id + ", name=" + name + ", author=" + author + ", categories=" + categories + ", isbn="
				+ isbn + ", publisher=" + publisher + "]";
	}

	public Book convertToBook() {
		Book book = new Book();
		book.setId(id);
		book.setTitle(name);
		book.setAuthor(author);
		book.setCategories(categories);
		book.setIsbn(isbn);
		book.setPublisher(publisher);
		return book;
	}

	public void copyTo(Book toModify) {
		toModify.setTitle(this.name);
		toModify.setAuthor(this.author);
		toModify.setCategories(this.categories);
		toModify.setIsbn(this.isbn);
	}

}
