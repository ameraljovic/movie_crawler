package ba.aljovic.amer.application.database;

import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImdbUsersRepository extends CrudRepository<ImdbUser, Long>
{
    @Query("select case when count(u) > 0 then true else false end from ImdbUser u where u.username = ?1")
    public Boolean existsByUsername(String username);
}
