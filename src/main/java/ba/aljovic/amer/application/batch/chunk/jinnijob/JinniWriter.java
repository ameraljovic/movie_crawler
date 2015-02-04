package ba.aljovic.amer.application.batch.chunk.jinnijob;

import ba.aljovic.amer.application.database.MovieFacade;
import ba.aljovic.amer.application.database.entities.jinnijob.Movie;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class JinniWriter implements ItemWriter<Movie>
{
    private MovieFacade movieFacade;

    @Autowired
    public JinniWriter(MovieFacade movieFacade)
    {
        this.movieFacade = movieFacade;
    }

    @Override
    public void write(List<? extends Movie> movies) throws Exception
    {
        movies.forEach(movieFacade::insertMovie);
    }
}
