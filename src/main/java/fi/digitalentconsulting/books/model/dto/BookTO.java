package fi.digitalentconsulting.books.model.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotBlank;

public class BookTO {
	private Long id;
	private String name;
	@NotBlank
	private String author;
	private List<Category> categories = new ArrayList<>();
	private String isbn;
	
	public BookTO() {
	}

	public BookTO(String name, String author, Collection<Category> categories, String isbn) {
		this.name = name;
		this.author = author;
		if (categories != null && !categories.isEmpty())
			this.categories.addAll(categories);
		this.isbn = isbn;
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

	@Override
	public String toString() {
		return "BookTO [id=" + id + ", name=" + name + ", author=" + author + ", category=" + categories + ", isbn="
				+ isbn + "]";
	}

}
