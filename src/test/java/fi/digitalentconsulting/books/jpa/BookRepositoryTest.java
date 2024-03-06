package fi.digitalentconsulting.books.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.EnumSet;

import jakarta.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import fi.digitalentconsulting.books.entity.Book;
import fi.digitalentconsulting.books.model.dto.Category;
import fi.digitalentconsulting.books.repository.BookRepository;

@DataJpaTest
public class BookRepositoryTest {
	@Autowired
	private TestEntityManager em;
	@Autowired
	private BookRepository repo;
	
	@Test
	public void testCreation() {
		assertNotNull(em);
		assertThat(repo.count()).isEqualTo(0);
		Book book = new Book();
		book.setTitle("Test title");
		book.setAuthor("Author Test");
		book.setCategories(EnumSet.of(Category.FICTION));
		book = repo.save(book);
		assertThat(repo.count()).isEqualTo(1);
		assertNotNull(em.find(Book.class, book.getId()));
	}
	
	@Test
	public void testIllegalCreation() {
		Book illegal = new Book();
		illegal.setAuthor("");
		repo.save(illegal);
		// The save in repository does not proceed to EntityManager flushing
		// so we don't get the Constraint violation we are expecting.
		// To overcome this:
		ConstraintViolationException ex = assertThrows(ConstraintViolationException.class, () -> {
			em.flush();
			fail();			
		});
		assertThat(ex.getMessage()).contains("Author");
	}
	
	
}
