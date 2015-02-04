package ba.aljovic.amer.application.batch.chunk.failedmoviesjob;

import ba.aljovic.amer.application.database.MovieFacade;
import ba.aljovic.amer.application.database.entities.jinnijob.Movie;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class FailedMoviesWriter implements ItemWriter<Movie>
{
    private MovieFacade movieFacade;

    public FailedMoviesWriter(MovieFacade movieFacade)
    {
        this.movieFacade = movieFacade;
    }

    @Override
    public void write(List<? extends Movie> movies) throws Exception
    {
        movies.forEach(movieFacade::recoverFailedMovie);
    }
}
