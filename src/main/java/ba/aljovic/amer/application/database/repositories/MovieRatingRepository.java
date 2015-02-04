package ba.aljovic.amer.application.database.repositories;

import ba.aljovic.amer.application.database.entities.userratingsjob.MovieRating;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRatingRepository extends CrudRepository<MovieRating, Long>
{
}
