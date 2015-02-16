package ba.aljovic.amer.application.batch.chunk;

import ba.aljovic.amer.application.database.repositories.ImdbUsersRepository;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImdbUsersPartitioner implements Partitioner
{
    public static final String FROM_ID = "fromId";
    public static final String TO_ID = "toId";
    private List<ImdbUser> users;
    private Long threadSize;

    public ImdbUsersPartitioner(ImdbUsersRepository repository, Long threadSize)
    {
        users = (List<ImdbUser>)repository.findAll();

        assert threadSize > 0;
        this.threadSize = threadSize;
    }

    @PostConstruct
    public void findUsers()
    {
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize)
    {
        Map<String, ExecutionContext> result = new HashMap<>();
        Long partitionSize = users.size() / threadSize;
        Long leftOver = users.size() - partitionSize * threadSize;
        Long fromId = users.get(0).getId();
        for (int i = 0; i < threadSize; i++)
        {
            Long toId = fromId + partitionSize - 1;
            if (i == threadSize - 1) toId += leftOver;
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
