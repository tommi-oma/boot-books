package fi.digitalentconsulting.books.repository;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import fi.digitalentconsulting.books.entity.Book;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, Long>{
	@Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
	Optional<Book> findById(Long id);
}
