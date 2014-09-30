package ba.aljovic.amer.batch.chunk;

import ba.aljovic.amer.database.MovieFacade;
import ba.aljovic.amer.database.entity.Movie;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class FailedMoviesWriter implements ItemWriter<Movie>
{
    @Autowired
    MovieFacade movieFacade;

    @Override
    public void write(List<? extends Movie> movies) throws Exception
    {
        for(Movie movie : movies)
        {
            movieFacade.recoverFailedMovie(movie);
        }
    }
}
