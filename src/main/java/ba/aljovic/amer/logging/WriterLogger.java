package ba.aljovic.amer.logging;

import ba.aljovic.amer.database.entity.Movie;
import ba.aljovic.amer.database.entity.StatusEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class WriterLogger
{
    private Logger logger = Logger.getLogger(getClass().toString());

    @Around ("execution(* ba.aljovic.amer.database.MovieFacade.insertMovie(ba.aljovic.amer.database.entity.Movie)) " +
            "&& args(movie)")
    public void insertMovie(ProceedingJoinPoint joinPoint, Movie movie)
    {
        try
        {
            joinPoint.proceed();

            logger.info("WRITER:Successfully inserted movie " + movie.getTitle() + " into database.");
        }
        catch (Throwable throwable)
        {
            logger.severe("WRITER:Movie '" + movie.getTitle() + " could not be inserted. ERROR MESSAGE:"
                    + throwable.getMessage());
        }

    }

    @Around("execution(* ba.aljovic.amer.database.MovieFacade.insertFailedMovie(String, String, boolean))" +
            "&& args(title, url, status)")
    public void insertFailedMovie(ProceedingJoinPoint joinPoint, String title, String url, boolean status)
    {
        try
        {
            joinPoint.proceed();

            logger.info("WRITER:Successfully inserted 'failed' movie '" + title + "' into database.");
        }
        catch (Throwable throwable)
        {
            logger.severe("WRITER:'Failed' movie '" + title + "' could not be inserted. ERROR MESSAGE:"
                    + throwable.getMessage());
        }
    }

    @Around("execution(* ba.aljovic.amer.database.MovieFacade.updateFailedMovieStatus(" +
            "Integer, ba.aljovic.amer.database.entity.StatusEnum)) && args(id, status)")
    public void updateFailedMovieStatus(ProceedingJoinPoint joinPoint, Integer id, StatusEnum status)
    {
        try
        {
            joinPoint.proceed();

            logger.info("WRITER:Successfully updated 'failed' movie with id=" + id + ".");
        }
        catch (Throwable throwable)
        {
            logger.severe("WRITER:'Failed' movie with id=" + id + " could not be updated. " +
                    "ERROR MESSAGE:" + throwable.getMessage());
        }
    }

    @Around("execution(* ba.aljovic.amer.database.MovieFacade.recoverFailedMovie(ba.aljovic.amer.database.entity.Movie)) " +
            "&& args(movie)")
    public void recoverFailedMovie(ProceedingJoinPoint joinPoint, Movie movie)
    {
        try
        {
            joinPoint.proceed();

            logger.info("WRITER: Successfully recovered failed movie '" + movie.getTitle() + "'.");
        }
        catch (Throwable throwable)
        {
            logger.severe("WRITER:Failed to recover movie '" + movie.getTitle() + "'. " +
                    "ERROR MESSAGE:" + throwable.getMessage());
        }
    }
}
