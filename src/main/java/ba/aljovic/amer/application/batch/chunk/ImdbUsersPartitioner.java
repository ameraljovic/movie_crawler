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
    public static final String FROM_ID = "fromId";
    public static final String TO_ID = "toId";
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
        assert gridSize > 0;
        Map<String, ExecutionContext> result = new HashMap<>();
        Integer partitionSize = users.size() / gridSize;
        Integer leftOver = users.size() - partitionSize * gridSize;
        Long fromId = users.get(0).getId();
        for (int i = 0; i < gridSize; i++)
        {
            Long toId = fromId + partitionSize - 1;
            if (i == gridSize - 1) toId += leftOver;
            ExecutionContext value = new ExecutionContext();
            value.putLong(FROM_ID, fromId);
            value.putLong(TO_ID, toId);
            value.putString("name", "Thread" + 1);
            result.put("partition" + (i + 1), value);

            fromId += partitionSize;
        }
        return result;
    }
}
