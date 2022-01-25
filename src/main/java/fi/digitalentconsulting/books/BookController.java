package fi.digitalentconsulting.books;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
	        })
	})
	@GetMapping("/{id}")
	public BookTO findBook(@Parameter(name = "id") @PathVariable Long id) {
		Optional<BookTO> book = bookService.findBook(id);
		if (!book.isPresent()) {
			LOGGER.warn("Book with id {} not found", id);
		}
		// will throw if not found, will fix later
		return book.get();
	}
	
	@Operation(summary = "Add a new book")
	@ApiResponses(value = {
			  @ApiResponse(responseCode = "200", description = "The created book",
			    content = { @Content(mediaType = "application/json", 
			                schema = @Schema(implementation = BookTO.class)) 
		        })
		})
	@PostMapping()
	public BookTO createBook(@io.swagger.v3.oas.annotations.parameters.RequestBody(
			content = @Content(mediaType = "application/json", 
			                   schema = @Schema(implementation= BookTO.class)))
			@RequestBody BookTO book) {
		BookTO created = bookService.add(book);
		return created;
	}
	
	@Operation(summary = "Delete a book")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteBook(@Parameter(name = "id", description = "id of the book") @PathVariable Long id) {
		BookTO dummy = new BookTO();
		dummy.setId(id);
		bookService.delete(dummy);
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
	public BookTO modifyBook(@Parameter(name = "id", description = "id of the book") @PathVariable Long id,
			@RequestBody BookTO book) {
		return bookService.modify(id, book);
	}
}
