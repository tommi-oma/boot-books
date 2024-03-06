package fi.digitalentconsulting.books.entity;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Publisher {
	@Id @GeneratedValue
	private Long id;

	private String name;
	private URL webpage;
	
	@JsonIgnore
	@OneToMany(mappedBy="publisher", cascade = {CascadeType.MERGE})
	private List<Book> books = new ArrayList<>();
	
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
	public URL getWebpage() {
		return webpage;
	}
	public void setWebpage(URL webpage) {
		this.webpage = webpage;
	}
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}
	public void addBook(Book book) {
		books.add(book);
		book.setPublisher(this);
	}
	@Override
	public String toString() {
		return "Publisher [id=" + id + ", name=" + name + ", webpage=" + webpage + "]";
	}
	
}
