package ba.aljovic.amer.application.batch.chunk.userratings;

import ba.aljovic.amer.application.database.UserRepository;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ImdbUserReader implements ItemStreamReader<ImdbUser>
{
    private static final String IMDB_USERS_COUNT = "imdb_user_count";
    private List<ImdbUser> users;

    @Autowired
    private UserRepository repository;

    private int readUsers;

    public ImdbUserReader(List<ImdbUser> users)
    {
        this.users = users;
    }

    @Override
    public ImdbUser read()
    {
        try
        {
            if (readUsers <= users.size())
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
