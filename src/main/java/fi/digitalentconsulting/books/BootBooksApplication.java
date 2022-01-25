package fi.digitalentconsulting.books;

import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fi.digitalentconsulting.books.model.dto.BookTO;
import fi.digitalentconsulting.books.model.dto.Category;
import fi.digitalentconsulting.books.service.BookService;

@SpringBootApplication
public class BootBooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootBooksApplication.class, args);
	}

	@Bean
	public ApplicationRunner initBooks(@Autowired BookService service) {
		return args -> {
			service.add(new BookTO("Book 1", "Author Arthur", EnumSet.of(Category.BIOGRAPHY), null));
			service.add(new BookTO("Book 2", "Author Beate", EnumSet.of(Category.POETRY, Category.COMPUTERS), "0-201-10088-6"));
			service.add(new BookTO("Book 3", "Author Clark", EnumSet.of(Category.BIOGRAPHY), null));
			service.add(new BookTO("Book 4", "Author Delilah", EnumSet.of(Category.COMPUTERS), null));
			service.findBooks().forEach(System.out::println);
		};
	}
}
