package fi.digitalentconsulting.books.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fi.digitalentconsulting.books.entity.Publisher;

@Repository
public interface PublisherRepository extends CrudRepository<Publisher, Long>{

}
