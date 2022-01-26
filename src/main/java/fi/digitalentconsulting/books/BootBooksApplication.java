package fi.digitalentconsulting.books;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import fi.digitalentconsulting.books.model.dto.BookTO;
import fi.digitalentconsulting.books.model.dto.Category;
import fi.digitalentconsulting.books.service.BookService;
import fi.digitalentconsulting.books.service.DatamuseService;

@SpringBootApplication
public class BootBooksApplication {
	private static Logger LOGGER = LoggerFactory.getLogger(BootBooksApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BootBooksApplication.class, args);
	}

	@Profile("!test")
	@Bean
	public ApplicationRunner initBooks(@Autowired BookService service) {
		return args -> {
			service.add(new BookTO("Book 1", "Author Arthur", EnumSet.of(Category.BIOGRAPHY), null));
			service.add(new BookTO("Book 2", "Author Beate", EnumSet.of(Category.POETRY, Category.COMPUTERS), "0-201-10088-6"));
			service.add(new BookTO("Book 3", "Author Clark", EnumSet.of(Category.BIOGRAPHY), null));
			service.add(new BookTO("Book 4", "Author Delilah", EnumSet.of(Category.COMPUTERS), null));
//			service.findBooks().forEach(System.out::println);
			service.findBooks().forEach(b->LOGGER.info("Book: {}", b));
		};
	}
	
//	@Bean
	public ApplicationRunner testDataMuse(@Autowired DatamuseService service) {
		return args -> {
			Arrays.asList("Spring", "Boot", "Spring Boot", "Book", "Compilers")
				.stream()
				.forEach(w-> {
					System.out.println("-- Word: " + w);
					try {
						service.getSynonyms(w).forEach(System.out::println);
					} catch (UnsupportedEncodingException e) {
						System.out.println("Encoding exception for: "+w);
					}
					System.out.println();
				});
			;
		};
	}
}
