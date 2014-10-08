package ba.aljovic.amer.database;

import ba.aljovic.amer.database.entity.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviesRepository extends CrudRepository<Movie, Integer>
{
}
