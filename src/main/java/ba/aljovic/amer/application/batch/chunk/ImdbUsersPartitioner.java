package ba.aljovic.amer.application.batch.chunk;

import ba.aljovic.amer.application.database.ImdbUsersRepository;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImdbUsersPartitioner implements Partitioner
{
    private List<ImdbUser> users;

    @Autowired
    private ImdbUsersRepository repository;

    @PostConstruct
    public void findUsers()
    {
        users = (List<ImdbUser>)repository.findAll();
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize)
    {
        return new HashMap<>();
    }
}
