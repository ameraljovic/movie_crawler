package ba.aljovic.amer.application.database;

import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImdbUsersRepository extends CrudRepository<ImdbUser, Integer>
{
}
