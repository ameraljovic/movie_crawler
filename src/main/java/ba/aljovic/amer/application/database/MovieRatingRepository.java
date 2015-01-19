package ba.aljovic.amer.application.database;

import ba.aljovic.amer.application.database.entities.userratingsjob.MovieRating;
import org.springframework.data.repository.CrudRepository;

public interface MovieRatingRepository extends CrudRepository<MovieRating, Long>
{
}
