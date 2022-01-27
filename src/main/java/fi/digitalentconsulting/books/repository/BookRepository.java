package fi.digitalentconsulting.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import fi.digitalentconsulting.books.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	@Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
	Optional<Book> findById(Long id);
}
