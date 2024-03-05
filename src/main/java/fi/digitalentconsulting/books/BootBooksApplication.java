package fi.digitalentconsulting.books;

import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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

	@Bean
	public ApplicationRunner initBooks(@Autowired BookService service) {
		return args -> {
			service.add(new BookTO("Java", "Author Arthur", EnumSet.of(Category.BIOGRAPHY), null));
			service.add(new BookTO("Spring", "Author Beate", EnumSet.of(Category.POETRY, Category.COMPUTERS), "0-201-10088-6"));
			service.add(new BookTO("Boot camp", "Author Clark", EnumSet.of(Category.BIOGRAPHY), null));
			service.add(new BookTO("Byte", "Author Delilah", EnumSet.of(Category.COMPUTERS), null));
//			service.findBooks().forEach(System.out::println);
			service.findBooks().forEach(b->LOGGER.info("Book: {}", b));
		};
	}
	
//	@Bean
	public ApplicationRunner testDataMuse(@Autowired DatamuseService service) {
		return args -> {
			service.getSynonyms("Spring").forEach(System.out::println);
		};
	}
}
