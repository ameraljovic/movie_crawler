package ba.aljovic.amer.database;

import ba.aljovic.amer.database.entity.FailedMovie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FailedMoviesRepository extends CrudRepository<FailedMovie, Integer>
{
    public List<FailedMovie> findByStatusId(Integer statusId);

    public FailedMovie findByTitle(String title);
}
