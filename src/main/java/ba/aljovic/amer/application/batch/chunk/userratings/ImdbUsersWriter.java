package ba.aljovic.amer.application.batch.chunk.userratings;

import ba.aljovic.amer.application.database.repositories.ImdbUsersRepository;
import ba.aljovic.amer.application.database.entities.userratingsjob.ImdbUser;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ImdbUsersWriter implements ItemWriter<List<ImdbUser>>
{
    @Autowired
    ImdbUsersRepository repository;

    @Override
    public void write(List<? extends List<ImdbUser>> items) throws Exception
    {
        for (List<ImdbUser> users : items)
        {
            for (ImdbUser user : users)
            {
                if (!repository.existsByUsername(user.getUsername()))
                {
                    repository.save(user);
                }
            }
        }
    }
}
