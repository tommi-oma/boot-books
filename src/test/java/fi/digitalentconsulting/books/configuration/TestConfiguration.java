package fi.digitalentconsulting.books.configuration;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import fi.digitalentconsulting.books.repository.BookRepository;
import fi.digitalentconsulting.books.service.BookService;
import fi.digitalentconsulting.books.service.BookServiceJPAImpl;
import fi.digitalentconsulting.books.service.DatamuseService;

@Profile("test")
//@Configuration    // Since this is commented out, Spring ignores the @Bean annotated method, and we use the
                    // TestDatamuseService that should now have a stereotype Service annotation
                    // So this class is used only as example code
public class TestConfiguration {
    @Bean @Primary
    public DatamuseService dataMuseService() {
        return Mockito.mock(DatamuseService.class);
    }
    
    @Bean
    public BookService bookService(@Autowired BookRepository repo) {
    	return new BookServiceJPAImpl(repo);
    }
}
