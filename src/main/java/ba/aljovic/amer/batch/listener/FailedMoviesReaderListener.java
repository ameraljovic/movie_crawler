package ba.aljovic.amer.batch.listener;

import ba.aljovic.amer.database.MovieFacade;
import ba.aljovic.amer.database.entity.Movie;
import ba.aljovic.amer.database.entity.StatusEnum;
import ba.aljovic.amer.exception.MovieNotFoundException;
import ba.aljovic.amer.exception.SuspiciousMovieException;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FailedMoviesReaderListener implements ItemReadListener<Movie>
{
    @Autowired
    MovieFacade movieFacade;

    @Override
    public void beforeRead()
    {
    }

    @Override
    public void afterRead(Movie item)
    {
    }

    @Override
    @Transactional (propagation = Propagation.REQUIRES_NEW)
    public void onReadError(Exception exception)
    {
        if (exception.getClass() == SuspiciousMovieException.class)
        {
            SuspiciousMovieException suspiciousMovieException = (SuspiciousMovieException)exception;
            movieFacade.updateFailedMovieStatus(suspiciousMovieException.getId(), StatusEnum.SUSPICIOUS);
        }
        else if (exception.getClass() == MovieNotFoundException.class)
        {
            MovieNotFoundException movieNotFoundException = (MovieNotFoundException)exception;
            movieFacade.updateFailedMovieStatus(movieNotFoundException.getId(), StatusEnum.FAILED);
        }
    }
}
