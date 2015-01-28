package ba.aljovic.amer.application.batch.chunk.userratings;

import ba.aljovic.amer.application.database.ImdbUsersRepository;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

public class ImdbUserReader implements ItemStreamReader<ImdbUser>
{
    private static final String IMDB_USERS_COUNT = "imdb_user_count";
    private final Long toId;
    private final Long fromId;
    private List<ImdbUser> users;
    private int readUsers;

    @Autowired
    private ImdbUsersRepository repository;


    public ImdbUserReader(Long fromId, Long toId)
    {
        this.fromId = fromId;
        this.toId = toId;
    }

    @PostConstruct
    public void findUsers()
    {
        users = repository.findUsersByRange(fromId, toId);
    }


    @Override
    public ImdbUser read()
    {
        try
        {
            if (readUsers <= users.size() && !users.isEmpty())
            {
                return users.get(readUsers);
            }
            return null;
        }
        finally
        {
            readUsers++;
        }
    }


    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException
    {
        if (executionContext.containsKey(IMDB_USERS_COUNT))
        {
            readUsers = executionContext.getInt(IMDB_USERS_COUNT);
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException
    {
        executionContext.putInt(IMDB_USERS_COUNT, readUsers);
    }

    @Override
    public void close() throws ItemStreamException
    {

    }
}
