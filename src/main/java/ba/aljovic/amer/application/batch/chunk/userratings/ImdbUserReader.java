package ba.aljovic.amer.application.batch.chunk.userratings;

import ba.aljovic.amer.application.database.repositories.ImdbUsersRepository;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ImdbUserReader implements ItemStreamReader<ImdbUser>
{
    private static final String IMDB_USERS_COUNT = "imdb_user_count";
    private final Long toId;
    private final Long fromId;
    private List<Long> userIds;
    private int userCount;

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
        userIds = new ArrayList<>();
        List<ImdbUser> users = repository.findUsersByRange(fromId, toId);
        userIds.addAll(users
                .stream()
                .map(ImdbUser::getId)
                .collect(Collectors.toList()));
    }


    @Override
    public ImdbUser read()
    {
        try
        {
            if (userCount <= userIds.size() && !userIds.isEmpty())
            {
                return repository.findOne(userIds.get(userCount));
            }
            return null;
        }
        finally
        {
            userCount++;
        }
    }


    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException
    {
        if (executionContext.containsKey(IMDB_USERS_COUNT))
        {
            userCount = executionContext.getInt(IMDB_USERS_COUNT);
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException
    {
        executionContext.putInt(IMDB_USERS_COUNT, userCount);
    }

    @Override
    public void close() throws ItemStreamException
    {

    }
}
