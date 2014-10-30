package ba.aljovic.amer.application.batch.chunk;

import ba.aljovic.amer.application.database.MovieFacade;
import ba.aljovic.amer.application.database.entity.Movie;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class JinniWriter implements ItemWriter<Movie>
{
    @Autowired
    private MovieFacade movieFacade;

    @Override
    public void write(List<? extends Movie> movies) throws Exception
    {
        for (Movie movie : movies)
        {
            movieFacade.insertMovie(movie);
        }
    }
}
