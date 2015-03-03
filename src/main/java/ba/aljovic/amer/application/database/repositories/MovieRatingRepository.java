package ba.aljovic.amer.application.database.repositories;

import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import ba.aljovic.amer.application.database.entities.userratingsjob.MovieRating;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRatingRepository extends CrudRepository<MovieRating, Long>
{
    public List<MovieRating> findByUser(ImdbUser user);
}
