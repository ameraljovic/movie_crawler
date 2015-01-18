package ba.aljovic.amer.application.batch.chunk.userratings;

import ba.aljovic.amer.application.database.ImdbMoviesRepository;
import ba.aljovic.amer.application.database.entity.ImdbMovie;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ImdbMoviesWriter implements ItemWriter<ImdbMovie>
{
    @Autowired
    ImdbMoviesRepository repository;

    @Override
    public void write(List<? extends ImdbMovie> movies) throws Exception
    {
        movies.forEach(repository::save);
    }
}
