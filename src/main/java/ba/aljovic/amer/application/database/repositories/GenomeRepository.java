package ba.aljovic.amer.application.database.repositories;

import ba.aljovic.amer.application.database.entities.jinnijob.Genome;
import ba.aljovic.amer.application.database.entities.jinnijob.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenomeRepository extends CrudRepository<Genome, Integer>
{
    public List<Genome> findByMovie(Movie movie);
}
