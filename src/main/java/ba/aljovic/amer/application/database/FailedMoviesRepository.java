package ba.aljovic.amer.application.database;

import ba.aljovic.amer.application.database.entities.failedmoviesjob.FailedMovie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FailedMoviesRepository extends CrudRepository<FailedMovie, Integer>
{
    public List<FailedMovie> findByStatusId(Integer statusId);

    public FailedMovie findByTitle(String title);
}
