package fi.digitalentconsulting.books.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fi.digitalentconsulting.books.entity.Publisher;
import fi.digitalentconsulting.books.model.dto.BookTO;
import fi.digitalentconsulting.books.repository.PublisherRepository;

@RestController
@RequestMapping("/api/v1/publishers")
public class PublisherController {
	private PublisherRepository publisherRepository;
	@Autowired
	public PublisherController(PublisherRepository publisherRepository) {
		this.publisherRepository = publisherRepository;
	}
	@GetMapping
	public ResponseEntity<?> allPublishers() {
		return ResponseEntity.ok(publisherRepository.findAll());
	}
	
	@GetMapping("/{id}/books")
	public ResponseEntity<?> booksForPublisher(@PathVariable Long id) {
		Publisher publisher = publisherRepository.findById(id).get();
		List<BookTO> books = publisher.getBooks().stream().map(b->new BookTO(b)).collect(Collectors.toList());
		return ResponseEntity.ok(books);
	}
}
