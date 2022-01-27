package fi.digitalentconsulting.books.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import fi.digitalentconsulting.books.entity.Book;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, Long>{

}
