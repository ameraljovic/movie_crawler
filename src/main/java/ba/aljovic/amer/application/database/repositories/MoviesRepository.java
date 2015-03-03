package ba.aljovic.amer.application.database.repositories;

import ba.aljovic.amer.application.database.entities.jinnijob.Movie;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoviesRepository extends CrudRepository<Movie, Integer>
{
    public Movie findByImdbId(String imdbId);

    @Query( "select m from Movie m " +
            "join m.ratings r " +
            "where r.user = :user")
    public List<Movie> findByUser(@Param("user") ImdbUser imdbUser);
}
