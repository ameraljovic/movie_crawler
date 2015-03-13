package ba.aljovic.amer.application.database.repositories;

import ba.aljovic.amer.application.database.entities.jinnijob.GenomeValue;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenomeValuesRepository extends CrudRepository<GenomeValue, Integer>
{
    @Query("select distinct g.name from GenomeValue g")
    public List<String> findDistinctNames();
}
