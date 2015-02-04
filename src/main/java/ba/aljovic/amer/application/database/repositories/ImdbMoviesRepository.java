package ba.aljovic.amer.application.database.repositories;

import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbMovie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImdbMoviesRepository extends CrudRepository<ImdbMovie, Integer>
{
}
