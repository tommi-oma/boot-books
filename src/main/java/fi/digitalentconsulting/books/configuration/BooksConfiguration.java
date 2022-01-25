package fi.digitalentconsulting.books.configuration;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import fi.digitalentconsulting.books.service.DatamuseService;

@Profile("!test")
@Configuration
public class BooksConfiguration {

	@Value("${wordservice.url.base:null}")
	private String baseUrl;
	
	@Value("${wordservice.url.words:null}")
	private String wordPart;
	
	@Bean
	public DatamuseService datamuseService() throws MalformedURLException {
		return new DatamuseService(baseUrl, wordPart);
	}
}
