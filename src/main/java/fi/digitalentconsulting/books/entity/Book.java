package fi.digitalentconsulting.books.entity;

import java.util.Collection;
import java.util.HashSet;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import fi.digitalentconsulting.books.model.dto.Category;

@Entity
public class Book {
	@Id @GeneratedValue
	private Long id;
	@NotNull
	@NotBlank(message = "Title can't be blank")
	private String title;
	@NotBlank(message = "Author can't be blank")
	private String author;
	// Price is not used in BookTO, a simple example of Entity having more info that DTO
	private Double price;
	private HashSet<Category> categories = new HashSet<>();
	private String isbn;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Collection<Category> getCategories() {
		return categories;
	}
	public void setCategories(Collection<Category> categories) {
		this.categories.clear();
		this.categories.addAll(categories);
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
}
