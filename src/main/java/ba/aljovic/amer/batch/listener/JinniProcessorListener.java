package ba.aljovic.amer.batch.listener;

import ba.aljovic.amer.database.MovieFacade;
import ba.aljovic.amer.database.entity.Movie;
import ba.aljovic.amer.database.entity.StatusEnum;
import ba.aljovic.amer.exception.JinniMovieNotFoundException;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JinniProcessorListener implements ItemProcessListener<Movie, Movie>
{
    @Autowired
    MovieFacade movieFacade;

    @Override
    public void beforeProcess(Movie item) {}

    @Override
    public void afterProcess(Movie item, Movie result) {}

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onProcessError(Movie movie, Exception exception)
    {
        if (exception.getClass() == JinniMovieNotFoundException.class)
            movieFacade.insertFailedMovie(movie, StatusEnum.UNKNOWN);
    }
}
