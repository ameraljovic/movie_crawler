package ba.aljovic.amer.application.database.repositories;

import ba.aljovic.amer.application.database.entities.machinelearning.GenomeMapping;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenomeMappingRepository extends CrudRepository<GenomeMapping, Long>
{
}
