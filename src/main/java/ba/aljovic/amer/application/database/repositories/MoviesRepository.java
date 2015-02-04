package ba.aljovic.amer.application.database.repositories;

import ba.aljovic.amer.application.database.entities.jinnijob.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviesRepository extends CrudRepository<Movie, Integer>
{
    public Movie findByImdbId(String imdbId);
}
