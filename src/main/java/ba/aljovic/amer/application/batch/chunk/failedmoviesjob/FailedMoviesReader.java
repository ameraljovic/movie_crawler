package ba.aljovic.amer.application.batch.chunk.failedmoviesjob;

import ba.aljovic.amer.application.component.service.MovieRetriever;
import ba.aljovic.amer.application.database.MovieFacade;
import ba.aljovic.amer.application.database.entity.FailedMovie;
import ba.aljovic.amer.application.database.entity.Movie;
import ba.aljovic.amer.application.exception.JinniMovieNotFoundException;
import ba.aljovic.amer.application.exception.SuspiciousMovieException;
import org.springframework.batch.item.ItemReader;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

public class FailedMoviesReader implements ItemReader<Movie>
{
    private List<FailedMovie> failedMovies;
    private int currentItemCount;
    private int maxItemCount;
    private MovieFacade movieFacade;
    private MovieRetriever movieRetriever;

    // Called first
    public void setMovieFacade(MovieFacade movieFacade)
    {
        this.movieFacade = movieFacade;
    }
    public void setMovieRetriever(MovieRetriever movieRetriever)
    {
        this.movieRetriever = movieRetriever;
    }

    // Called second
    @PostConstruct
    public void init()
    {
        failedMovies = movieFacade.getFailedMovies();
        currentItemCount = 0;
        maxItemCount = failedMovies.size();
    }

    @Override
    public Movie read() throws Exception
    {
        if (currentItemCount >= maxItemCount) return null;
        FailedMovie fm = failedMovies.get(currentItemCount);
        try
        {
            String url = movieRetriever.retrieveUrlBySearch(fm.getTitle());
            return new Movie(fm.getTitle(), fm.getImdbId(), fm.getTmdbId(), url);
        }
        catch (SuspiciousMovieException e)
        {
            throw new SuspiciousMovieException(fm.getId());
        }
        catch (JinniMovieNotFoundException e)
        {
            throw new JinniMovieNotFoundException(fm.getTitle(), fm.getUrl(), fm.getId());
        }
        catch (IOException e )
        {
            // Retry
            currentItemCount--;
            throw e;
        }
        finally
        {
            if (currentItemCount <= maxItemCount)
                currentItemCount++;
        }
    }
}
