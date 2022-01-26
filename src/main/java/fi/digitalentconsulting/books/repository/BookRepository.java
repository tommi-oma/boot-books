package fi.digitalentconsulting.books.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fi.digitalentconsulting.books.entity.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Long>{

}
