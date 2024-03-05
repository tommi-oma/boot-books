package fi.digitalentconsulting.books;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import fi.digitalentconsulting.books.util.ExceptionMessage;

@ControllerAdvice(assignableTypes = BookController.class)
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class BookControllerAdvice {
	private static Logger LOGGER = LoggerFactory.getLogger(BookControllerAdvice.class);
    @ExceptionHandler(value = { NoSuchElementException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ExceptionMessage> handleNoSuchElement(
      RuntimeException ex, WebRequest request) {
    	LOGGER.info("Exception '{}' returns NOT_FOUND", ex.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
        		.body(new ExceptionMessage("Not found", ex.getMessage()));
    }
    
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ExceptionMessage> handleValidationException(
    		MethodArgumentNotValidException ex, WebRequest request) {
    	String errmessage = ex.getBindingResult().getAllErrors()
    			.stream()
    			.map(ObjectError::getDefaultMessage)
    			.collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        		.body(new ExceptionMessage("Bad request", errmessage));
    }

}
