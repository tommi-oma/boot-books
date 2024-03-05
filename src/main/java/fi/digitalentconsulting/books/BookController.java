package fi.digitalentconsulting.books;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fi.digitalentconsulting.books.model.dto.BookTO;
import fi.digitalentconsulting.books.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
	private static Logger LOGGER = LoggerFactory.getLogger(BookController.class);
	private BookService bookService;
	
	@Autowired
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}
	@Operation(summary = "Retrieve all books")
	@ApiResponses(value = {
			  @ApiResponse(responseCode = "200", description = "List of books", 
			    content = { @Content(mediaType = "application/json", 
			        array = @ArraySchema(schema = @Schema(implementation = BookTO.class))) 
		        })
		})
	@GetMapping()
	public List<BookTO> books() {
		return bookService.findBooks();
	}
	
	@Operation(summary = "Find one book")
	@ApiResponses(value = {
		  @ApiResponse(responseCode = "200", description = "A book", 
		    content = { @Content(mediaType = "application/json", 
		                schema = @Schema(implementation = BookTO.class)) 
	        }),
		  @ApiResponse(responseCode = "404", description = "Book not found")
	})
	@GetMapping("/{id}")
	public ResponseEntity<?> findBook(@Parameter(name = "id") @PathVariable Long id) {
		Optional<BookTO> book = bookService.findBook(id);
		if (!book.isPresent()) {
			LOGGER.warn("Book with id {} not found", id);
			// as an example just return 404 NOT_FOUND
			return ResponseEntity.notFound().build();
		}
		// will throw if not found, will fix later
		return ResponseEntity.ok(book.get());
	}
	
	@Operation(summary = "Add a new book")
	@ApiResponses(value = {
			  @ApiResponse(responseCode = "200", description = "The created book",
			    content = { @Content(mediaType = "application/json", 
			                schema = @Schema(implementation = BookTO.class)) 
		        })
		})
	@PostMapping()
	public ResponseEntity<?> createBook(@io.swagger.v3.oas.annotations.parameters.RequestBody(
			content = @Content(mediaType = "application/json", 
			                   schema = @Schema(implementation= BookTO.class)))
			@RequestBody BookTO book) {
		BookTO created = bookService.add(book);
		URI location = ServletUriComponentsBuilder
	            .fromCurrentRequest().path("/{id}")
	            .buildAndExpand(created.getId()).toUri();
		return ResponseEntity.created(location).body(created);
	}
	
	@Operation(summary = "Delete a book")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<?> deleteBook(@Parameter(name = "id", description = "id of the book") @PathVariable Long id) {
		BookTO dummy = new BookTO();
		dummy.setId(id);
		bookService.delete(dummy);
		return ResponseEntity.noContent().build();
	}
	
	@Operation(summary = "Update a book", description = "Updates a book that is found with the id parameter. The book in the body has its id ignored. "+
	"Note that the existing book will have all its values updated, apart from the id")
	@ApiResponses(value = {
			  @ApiResponse(responseCode = "200", description = "The modified book", 
			    content = { @Content(mediaType = "application/json", 
			                schema = @Schema(implementation = BookTO.class)) 
		        })
		})
	@PutMapping("/{id}")
	public ResponseEntity<BookTO> modifyBook(@Parameter(name = "id", description = "id of the book") @PathVariable Long id,
			@Valid @RequestBody BookTO book) {  // @Valid does not validate, unless we have validation dependency
		// BookService::modify will throw NoSuchElementException if book not found.
		// ControllerAdvice will handle the exception with 404 NOT_FOUND
		BookTO modified = bookService.modify(id, book);
		return ResponseEntity.ok(modified);
	}
}
