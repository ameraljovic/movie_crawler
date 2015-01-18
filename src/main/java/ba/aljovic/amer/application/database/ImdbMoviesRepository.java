package ba.aljovic.amer.application.database;

import ba.aljovic.amer.application.database.entity.ImdbMovie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImdbMoviesRepository extends CrudRepository<ImdbMovie, Integer>
{
}
