package ba.aljovic.amer.application.database;

import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImdbUsersRepository extends CrudRepository<ImdbUser, Long>
{
    @Query("select case when count(u) > 0 then true else false end from ImdbUser u where u.username = ?1")
    public Boolean existsByUsername(String username);

    @Query("select u from ImdbUser u where id between ?1 and ?2")
    public List<ImdbUser> findUsersByRange(Long fromId, Long toId);
}
