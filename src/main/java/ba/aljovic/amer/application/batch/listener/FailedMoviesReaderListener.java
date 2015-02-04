package ba.aljovic.amer.application.batch.listener;

import ba.aljovic.amer.application.database.MovieFacade;
import ba.aljovic.amer.application.database.entities.failedmoviesjob.StatusEnum;
import ba.aljovic.amer.application.database.entities.jinnijob.Movie;
import ba.aljovic.amer.application.exception.JinniMovieNotFoundException;
import ba.aljovic.amer.application.exception.SuspiciousMovieException;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FailedMoviesReaderListener implements ItemReadListener<Movie>
{
    private MovieFacade movieFacade;

    @Autowired
    public FailedMoviesReaderListener(MovieFacade movieFacade)
    {
        this.movieFacade = movieFacade;
    }

    @Override
    public void beforeRead() {}

    @Override
    public void afterRead(Movie item) {}

    @Override
    @Transactional (propagation = Propagation.REQUIRES_NEW)
    public void onReadError(Exception exception)
    {
        if (exception.getClass() == SuspiciousMovieException.class)
        {
            SuspiciousMovieException suspiciousMovieException = (SuspiciousMovieException)exception;
            movieFacade.updateFailedMovieStatus(suspiciousMovieException.getId(), StatusEnum.SUSPICIOUS);
        }
        else if (exception.getClass() == JinniMovieNotFoundException.class)
        {
            JinniMovieNotFoundException jinniMovieNotFoundException = (JinniMovieNotFoundException)exception;
            movieFacade.updateFailedMovieStatus(jinniMovieNotFoundException.getId(), StatusEnum.FAILED);
        }
    }
}
