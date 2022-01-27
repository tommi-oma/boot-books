package fi.digitalentconsulting.books.repository;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fi.digitalentconsulting.books.entity.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Long>{
	@Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
	Optional<Book> findById(Long id);
}
