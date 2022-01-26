package fi.digitalentconsulting.books.configuration;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import fi.digitalentconsulting.books.repository.BookRepository;
import fi.digitalentconsulting.books.service.BookService;
import fi.digitalentconsulting.books.service.BookServiceJPAImpl;
import fi.digitalentconsulting.books.service.BookServiceMapImpl;
import fi.digitalentconsulting.books.service.DatamuseService;

@Profile("!test")
@Configuration
public class BooksConfiguration {

	@Value("${wordservice.url.base:null}")
	private String baseUrl;
	
	@Value("${wordservice.url.words:null}")
	private String wordPart;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Bean
	public DatamuseService datamuseService() throws MalformedURLException {
		return new DatamuseService(baseUrl, wordPart);
	}
	
	@Bean
	public BookService mapService() {
		BookService service = new BookServiceMapImpl();
		return service;
	}
	
	@Bean @Primary
	public BookService jpaService() {
		return new BookServiceJPAImpl(bookRepository);
	}
}
